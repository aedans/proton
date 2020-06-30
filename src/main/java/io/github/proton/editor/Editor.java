package io.github.proton.editor;

import io.vavr.collection.Map;
import io.vavr.control.Option;

import java.awt.event.KeyEvent;

public record Editor<T>(Style style, Projector<T>projector, T tree, Position cursor, int width) {
    public static <T> Option<Char<T>> character(Map<Position, Char<T>> characters, Position selected) {
        return characters.get(selected);
    }

    public static <T> Position selected(Map<Position, Char<T>> characters, Position cursor) {
        return characters
            .get(cursor)
            .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor))
            .orElse(() -> left1(characters, cursor))
            .orElse(() -> right1(characters, cursor))
            .getOrElse(cursor.withCol(0));
    }

    public static <T> Option<Position> left1(Map<Position, Char<T>> characters, Position cursor) {
        if (cursor.col() < 0) return Option.none();
        var cursor1 = cursor.withRelativeCol(-1);
        return characters
            .get(cursor1)
            .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor1))
            .orElse(() -> left1(characters, cursor1));
    }

    public static <T> Option<Position> left(Map<Position, Char<T>> characters, Position cursor) {
        if (cursor.row() < 0) return Option.none();
        return left1(characters, cursor)
            .orElse(() -> left(characters, cursor.withRelativeRow(-1).withCol(cols(characters))));
    }

    public static <T> Option<Position> right1(Map<Position, Char<T>> characters, Position cursor) {
        if (cursor.col() > cols(characters)) return Option.none();
        var cursor1 = cursor.withRelativeCol(1);
        return characters
            .get(cursor1)
            .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor1))
            .orElse(() -> right1(characters, cursor1));
    }

    public static <T> Option<Position> right(Map<Position, Char<T>> characters, Position cursor) {
        if (cursor.row() > rows(characters)) return Option.none();
        return right1(characters, cursor).orElse(() -> right(characters, cursor.withRelativeRow(1).withCol(-1)));
    }

    public static <T> Option<Position> up(Map<Position, Char<T>> characters, Position cursor) {
        if (cursor.row() <= 0) return Option.none();
        var cursor1 = cursor.withRelativeRow(-1);
        return characters
            .get(cursor1)
            .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor1))
            .orElse(() -> left1(characters, cursor1).orElse(right1(characters, cursor1)).map(x -> cursor1))
            .orElse(() -> up(characters, cursor1));
    }

    public static <T> Option<Position> down(Map<Position, Char<T>> characters, Position cursor) {
        if (cursor.row() >= rows(characters) - 1) return Option.none();
        var cursor1 = cursor.withRelativeRow(1);
        return characters
            .get(cursor1)
            .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor1))
            .orElse(() -> right1(characters, cursor1).orElse(left1(characters, cursor1)).map(x -> cursor1))
            .orElse(() -> down(characters, cursor1));
    }

    public Editor<T> update(KeyEvent key) {
        var characters = projector.project(tree).characters(width);
        var selected = selected(characters, cursor);
        if (characters.isEmpty()) {
            return this;
        }
        return switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT ->
                new Editor<>(style, projector, tree, left(characters, selected).getOrElse(cursor), width);
            case KeyEvent.VK_RIGHT ->
                new Editor<>(style, projector, tree, right(characters, selected).getOrElse(cursor), width);
            case KeyEvent.VK_UP ->
                new Editor<>(style, projector, tree, up(characters, cursor).getOrElse(cursor), width);
            case KeyEvent.VK_DOWN ->
                new Editor<>(style, projector, tree, down(characters, cursor).getOrElse(cursor), width);
            case KeyEvent.VK_DELETE -> delete(characters, selected);
            case KeyEvent.VK_BACK_SPACE -> backspace(characters, selected);
            case KeyEvent.VK_ENTER -> enter(characters, selected);
            case KeyEvent.VK_SHIFT, KeyEvent.VK_ALT, KeyEvent.VK_CAPS_LOCK -> this;
            default -> insert(key, characters, selected);
        };
    }

    private Editor<T> delete(Map<Position, Char<T>> characters, Position selected) {
        return character(characters, selected)
            .map(character -> {
                T t2 = character.delete().getOrElse(tree);
                return new Editor<>(style, projector, t2, selected, width);
            })
            .getOrElse(this);
    }

    private Editor<T> backspace(Map<Position, Char<T>> characters, Position selected) {
        var deleted = left(characters, selected).getOrElse(cursor);
        return character(characters, deleted)
            .flatMap(c -> c.decorative() ? Option.none() : Option.some(c))
            .map(character -> {
                T t2 = character.delete().getOrElse(tree);
                return new Editor<>(style, projector, t2, deleted, width);
            })
            .getOrElse(new Editor<>(style, projector, tree, left(characters, selected).getOrElse(cursor), width));
    }

    private Editor<T> enter(Map<Position, Char<T>> characters, Position selected) {
        return character(characters, selected)
            .map(character -> {
                T t2 = character.insert('\n').getOrElse(tree);
                var characters2 = projector.project(t2).characters(width);
                return new Editor<>(
                    style,
                    projector,
                    t2,
                    right(characters2, cursor.withRelativeRow(1).withCol(-1)).getOrElse(cursor),
                    width);
            })
            .getOrElse(new Editor<>(style, projector, tree, right(characters, selected).getOrElse(cursor), width));
    }

    private Editor<T> insert(KeyEvent key, Map<Position, Char<T>> characters, Position selected) {
        return character(characters, selected)
            .map(character -> character
                .insert(key.getKeyChar())
                .map(t2 -> {
                    var characters2 = projector.project(t2).characters(width);
                    return new Editor<>(style, projector, t2, right1(characters2, selected).getOrElse(cursor), width);
                })
                .getOrElse(new Editor<>(style, projector, tree, right1(characters, selected).getOrElse(cursor), width)))
            .getOrElse(this);
    }

    private static <T> int rows(Map<Position, Char<T>> characters) {
        return characters.mapKeys(x -> x.row() + 1).keySet().max().getOrElse(0);
    }

    private static <T> int cols(Map<Position, Char<T>> characters) {
        return characters.mapKeys(x -> x.col() + 1).keySet().max().getOrElse(0);
    }

    public void render(Display<T> display) {
        var characters = projector.project(tree).characters(width);
        display.setBackgroundColor(style.background());
        display.setCharacters(characters.mapValues(x -> x.character(style)));
        display.setCursor(selected(characters, cursor));
    }
}
