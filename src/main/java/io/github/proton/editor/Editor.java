/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.editor;

import io.vavr.control.Option;
import java.awt.event.KeyEvent;

public final class Editor<T> {
    private final Style style;
    private final Projector<T> projector;
    private final T tree;
    private final Position cursor;

    public Editor(Style style, Projector<T> projector, T tree, Position cursor) {
        this.style = style;
        this.projector = projector;
        this.tree = tree;
        this.cursor = cursor;
    }

    public static <T> Option<Projection.Char<T>> character(Projection<T> projection, Position selected) {
        return projection.characters().get(selected);
    }

    public static <T> Position selected(Projection<T> projection, Position cursor) {
        return projection
                .characters()
                .get(cursor)
                .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor))
                .orElse(() -> left1(projection, cursor))
                .orElse(() -> right1(projection, cursor))
                .getOrElse(cursor.withColumn(0));
    }

    public static <T> Option<Position> left1(Projection<T> projection, Position cursor) {
        if (cursor.getColumn() < 0) return Option.none();
        Position cursor1 = cursor.withRelativeColumn(-1);
        return projection
                .characters()
                .get(cursor1)
                .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor1))
                .orElse(() -> left1(projection, cursor1));
    }

    public static <T> Option<Position> left(Projection<T> projection, Position cursor) {
        if (cursor.getRow() < 0) return Option.none();
        return left1(projection, cursor)
                .orElse(() -> left(projection, cursor.withRelativeRow(-1).withColumn(projection.columns())));
    }

    public static <T> Option<Position> right1(Projection<T> projection, Position cursor) {
        if (cursor.getColumn() > projection.columns()) return Option.none();
        Position cursor1 = cursor.withRelativeColumn(1);
        return projection
                .characters()
                .get(cursor1)
                .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor1))
                .orElse(() -> right1(projection, cursor1));
    }

    public static <T> Option<Position> right(Projection<T> projection, Position cursor) {
        if (cursor.getRow() > projection.rows()) return Option.none();
        return right1(projection, cursor).orElse(() -> right(projection, cursor.withRelativeRow(1).withColumn(-1)));
    }

    public static <T> Option<Position> up(Projection<T> projection, Position cursor) {
        if (cursor.getRow() <= 0) return Option.none();
        Position cursor1 = cursor.withRelativeRow(-1);
        return projection
                .characters()
                .get(cursor1)
                .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor1))
                .orElse(() -> left1(projection, cursor1).orElse(right1(projection, cursor1)).map(x -> cursor1))
                .orElse(() -> up(projection, cursor1));
    }

    public static <T> Option<Position> down(Projection<T> projection, Position cursor) {
        if (cursor.getRow() >= projection.rows() - 1) return Option.none();
        Position cursor1 = cursor.withRelativeRow(1);
        return projection
                .characters()
                .get(cursor1)
                .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor1))
                .orElse(() -> right1(projection, cursor1).orElse(left1(projection, cursor1)).map(x -> cursor1))
                .orElse(() -> down(projection, cursor1));
    }

    public Editor<T> update(KeyEvent key) {
        Projection<T> projection = projector.project(tree);
        Position selected = selected(projection, cursor);
        if (projection.characters().isEmpty()) {
            return this;
        }
        switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                return new Editor<>(style, projector, tree, left(projection, selected).getOrElse(cursor));
            case KeyEvent.VK_RIGHT:
                return new Editor<>(style, projector, tree, right(projection, selected).getOrElse(cursor));
            case KeyEvent.VK_UP:
                return new Editor<>(style, projector, tree, up(projection, cursor).getOrElse(cursor));
            case KeyEvent.VK_DOWN:
                return new Editor<>(style, projector, tree, down(projection, cursor).getOrElse(cursor));
            case KeyEvent.VK_DELETE:
                return character(projection, selected)
                        .map(character -> {
                            T t2 = character.delete().getOrElse(tree);
                            return new Editor<>(style, projector, t2, selected);
                        })
                        .getOrElse(this);
            case KeyEvent.VK_BACK_SPACE:
                Position deleted = cursor.getColumn() <= 0
                        ? selected(projection, cursor.withRelativeRow(-1).withColumn(projection.columns()))
                        : selected.withRelativeColumn(-1);
                return character(projection, deleted)
                        .flatMap(c -> c.decorative() ? Option.none() : Option.some(c))
                        .map(character -> {
                            T t2 = character.delete().getOrElse(tree);
                            return new Editor<>(style, projector, t2, deleted);
                        })
                        .getOrElse(new Editor<>(style, projector, tree, left(projection, selected).getOrElse(cursor)));
            case KeyEvent.VK_ENTER:
                return character(projection, selected)
                        .map(character -> {
                            T t2 = character.insert('\n').getOrElse(tree);
                            return new Editor<>(
                                    style,
                                    projector,
                                    t2,
                                    right(projector.project(t2), cursor.withRelativeRow(1).withColumn(-1))
                                            .getOrElse(cursor));
                        })
                        .getOrElse(new Editor<>(style, projector, tree, right(projection, selected).getOrElse(cursor)));
            case KeyEvent.VK_SHIFT:
            case KeyEvent.VK_ALT:
            case KeyEvent.VK_CAPS_LOCK:
                return this;
            default:
                return character(projection, selected)
                        .map(character -> character
                                .insert(key.getKeyChar())
                                .map(t2 -> new Editor<>(
                                        style, projector, t2, right(projector.project(t2), selected).getOrElse(cursor)))
                                .getOrElse(
                                        character.character(style).character == key.getKeyChar()
                                                ? new Editor<>(
                                                        style,
                                                        projector,
                                                        tree,
                                                        right(projector.project(tree), selected)
                                                                .getOrElse(cursor))
                                                : this))
                        .getOrElse(this);
        }
    }

    public void render(Display<T> display) {
        display.setBackgroundColor(style.background());
        display.setCharacters(projector.project(tree).characters().mapValues(x -> x.character(style)));
        display.setCursor(selected(projector.project(tree), cursor));
    }
}
