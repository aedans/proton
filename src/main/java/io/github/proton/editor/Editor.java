package io.github.proton.editor;

import io.vavr.control.Option;

import java.awt.event.KeyEvent;

public record Editor<T>(Style style, Projector<T>projector, T tree, Position cursor) {
    public static <T> Option<Char<T>> character(Projection<T> projection, Position selected) {
        return projection.characters().get(selected);
    }

    public static <T> Position selected(Projection<T> projection, Position cursor) {
        return projection
            .characters()
            .get(cursor)
            .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor))
            .orElse(() -> left1(projection, cursor))
            .orElse(() -> right1(projection, cursor))
            .getOrElse(cursor.withCol(0));
    }

    public static <T> Option<Position> left1(Projection<T> projection, Position cursor) {
        if (cursor.col() < 0) return Option.none();
        var cursor1 = cursor.withRelativeCol(-1);
        return projection
            .characters()
            .get(cursor1)
            .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor1))
            .orElse(() -> left1(projection, cursor1));
    }

    public static <T> Option<Position> left(Projection<T> projection, Position cursor) {
        if (cursor.row() < 0) return Option.none();
        return left1(projection, cursor)
            .orElse(() -> left(projection, cursor.withRelativeRow(-1).withCol(projection.columns())));
    }

    public static <T> Option<Position> right1(Projection<T> projection, Position cursor) {
        if (cursor.col() > projection.columns()) return Option.none();
        var cursor1 = cursor.withRelativeCol(1);
        return projection
            .characters()
            .get(cursor1)
            .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor1))
            .orElse(() -> right1(projection, cursor1));
    }

    public static <T> Option<Position> right(Projection<T> projection, Position cursor) {
        if (cursor.row() > projection.rows()) return Option.none();
        return right1(projection, cursor).orElse(() -> right(projection, cursor.withRelativeRow(1).withCol(-1)));
    }

    public static <T> Option<Position> up(Projection<T> projection, Position cursor) {
        if (cursor.row() <= 0) return Option.none();
        var cursor1 = cursor.withRelativeRow(-1);
        return projection
            .characters()
            .get(cursor1)
            .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor1))
            .orElse(() -> left1(projection, cursor1).orElse(right1(projection, cursor1)).map(x -> cursor1))
            .orElse(() -> up(projection, cursor1));
    }

    public static <T> Option<Position> down(Projection<T> projection, Position cursor) {
        if (cursor.row() >= projection.rows() - 1) return Option.none();
        var cursor1 = cursor.withRelativeRow(1);
        return projection
            .characters()
            .get(cursor1)
            .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor1))
            .orElse(() -> right1(projection, cursor1).orElse(left1(projection, cursor1)).map(x -> cursor1))
            .orElse(() -> down(projection, cursor1));
    }

    public Editor<T> update(KeyEvent key) {
        var projection = projector.project(tree);
        var selected = selected(projection, cursor);
        if (projection.characters().isEmpty()) {
            return this;
        }
        return switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT -> new Editor<>(style, projector, tree, left(projection, selected).getOrElse(cursor));
            case KeyEvent.VK_RIGHT -> new Editor<>(style, projector, tree, right(projection, selected).getOrElse(cursor));
            case KeyEvent.VK_UP -> new Editor<>(style, projector, tree, up(projection, cursor).getOrElse(cursor));
            case KeyEvent.VK_DOWN -> new Editor<>(style, projector, tree, down(projection, cursor).getOrElse(cursor));
            case KeyEvent.VK_DELETE -> delete(projection, selected);
            case KeyEvent.VK_BACK_SPACE -> backspace(projection, selected);
            case KeyEvent.VK_ENTER -> enter(projection, selected);
            case KeyEvent.VK_SHIFT, KeyEvent.VK_ALT, KeyEvent.VK_CAPS_LOCK -> this;
            default -> insert(key, projection, selected);
        };
    }

    private Editor<T> delete(Projection<T> projection, Position selected) {
        return character(projection, selected)
            .map(character -> {
                T t2 = character.delete().getOrElse(tree);
                return new Editor<>(style, projector, t2, selected);
            })
            .getOrElse(this);
    }

    private Editor<T> backspace(Projection<T> projection, Position selected) {
        var deleted = left(projection, selected).getOrElse(cursor);
        return character(projection, deleted)
            .flatMap(c -> c.decorative() ? Option.none() : Option.some(c))
            .map(character -> {
                T t2 = character.delete().getOrElse(tree);
                return new Editor<>(style, projector, t2, deleted);
            })
            .getOrElse(new Editor<>(style, projector, tree, left(projection, selected).getOrElse(cursor)));
    }

    private Editor<T> enter(Projection<T> projection, Position selected) {
        return character(projection, selected)
            .map(character -> {
                T t2 = character.insert('\n').getOrElse(tree);
                return new Editor<>(
                    style,
                    projector,
                    t2,
                    right(projector.project(t2), cursor.withRelativeRow(1).withCol(-1))
                        .getOrElse(cursor));
            })
            .getOrElse(new Editor<>(style, projector, tree, right(projection, selected).getOrElse(cursor)));
    }

    private Editor<T> insert(KeyEvent key, Projection<T> projection, Position selected) {
        return character(projection, selected)
            .map(character -> character
                .insert(key.getKeyChar())
                .map(t2 -> new Editor<>(style, projector, t2, right1(projector.project(t2), selected).getOrElse(cursor)))
                .getOrElse(new Editor<>(style, projector, tree, right1(projector.project(tree), selected).getOrElse(cursor))))
            .getOrElse(this);
    }

    public void render(Display<T> display) {
        display.setBackgroundColor(style.background());
        display.setCharacters(projector.project(tree).characters().mapValues(x -> x.character(style)));
        display.setCursor(selected(projector.project(tree), cursor));
    }
}
