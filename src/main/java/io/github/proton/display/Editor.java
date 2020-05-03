/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.display;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.input.KeyStroke;
import io.vavr.control.Option;

public final class Editor<T> {
    private final Style style;
    private final Projector<T> projector;
    private final T tree;
    private final TerminalPosition cursor;

    public Editor(Style style, Projector<T> projector, T tree, TerminalPosition cursor) {
        this.style = style;
        this.projector = projector;
        this.tree = tree;
        this.cursor = cursor;
    }

    public static <T> TerminalPosition selected(Projection<T> projection, TerminalPosition cursor) {
        return projection
                .characters
                .get(cursor)
                .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor))
                .orElse(() -> left(projection, cursor))
                .orElse(() -> right(projection, cursor))
                .getOrElse(cursor.withColumn(0));
    }

    public static <T> Option<Projection.Char<T>> character(Projection<T> projection, TerminalPosition selected) {
        return projection.characters.get(selected);
    }

    public static <T> Option<TerminalPosition> left(Projection<T> projection, TerminalPosition cursor) {
        if (cursor.getRow() < 0) return Option.none();
        if (cursor.getColumn() < 0)
            return left(projection, cursor.withRelativeRow(-1).withColumn(projection.columns()));
        TerminalPosition cursor1 = cursor.withRelativeColumn(-1);
        return projection
                .characters
                .get(cursor1)
                .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor1))
                .orElse(() -> left(projection, cursor1));
    }

    public static <T> Option<TerminalPosition> right(Projection<T> projection, TerminalPosition cursor) {
        if (cursor.getRow() > projection.rows()) return Option.none();
        if (cursor.getColumn() > projection.columns())
            return right(projection, cursor.withRelativeRow(1).withColumn(-1));
        TerminalPosition cursor1 = cursor.withRelativeColumn(1);
        return projection
                .characters
                .get(cursor1)
                .flatMap(c -> c.decorative() ? Option.none() : Option.some(cursor1))
                .orElse(() -> right(projection, cursor1));
    }

    public Editor<T> update(KeyStroke keyStroke) {
        Projection<T> projection = projector.project(tree);
        TerminalPosition selected = selected(projection, cursor);
        if (projection.characters.isEmpty()) {
            return this;
        }
        switch (keyStroke.getKeyType()) {
            case ArrowLeft:
                return new Editor<>(style, projector, tree, left(projection, selected).getOrElse(cursor));
            case ArrowRight:
                return new Editor<>(style, projector, tree, right(projection, selected).getOrElse(cursor));
            case ArrowUp:
                return new Editor<>(style, projector, tree, cursor.withRow(Math.max(0, cursor.getRow() - 1)));
            case ArrowDown:
                return new Editor<>(
                        style, projector, tree, cursor.withRow(Math.min(projection.rows() - 1, cursor.getRow() + 1)));
            case Character:
                return character(projection, selected)
                        .map(character -> {
                            T t2 = character.insert(keyStroke.getCharacter()).getOrElse(tree);
                            return new Editor<>(
                                    style, projector, t2, right(projector.project(t2), selected).getOrElse(cursor));
                        })
                        .getOrElse(this);
            case Delete:
                return character(projection, selected)
                        .map(character -> {
                            T t2 = character.delete().getOrElse(tree);
                            return new Editor<>(style, projector, t2, selected);
                        })
                        .getOrElse(this);
            case Backspace:
                return character(projection, selected.withRelativeColumn(-1))
                        .map(character -> {
                            T t2 = character.delete().getOrElse(tree);
                            return new Editor<>(
                                    style, projector, t2, left(projector.project(t2), selected).getOrElse(cursor));
                        })
                        .getOrElse(new Editor<>(style, projector, tree, left(projection, selected).getOrElse(cursor)));
            case Enter:
                return character(projection, selected)
                        .map(character -> {
                            T t2 = character.submit().getOrElse(tree);
                            return new Editor<>(
                                    style,
                                    projector,
                                    t2,
                                    right(projector.project(t2), cursor.withRelativeRow(1).withColumn(-1))
                                            .getOrElse(cursor));
                        })
                        .getOrElse(new Editor<>(style, projector, tree, right(projection, selected).getOrElse(cursor)));
            default:
                return this;
        }
    }

    public void render(TerminalDisplay display) {
        display.background(style.base(' '));
        projector.project(tree).characters.forEach(c -> display.write(c._2.character(style), c._1));
        display.setCursor(selected(projector.project(tree), cursor));
    }
}
