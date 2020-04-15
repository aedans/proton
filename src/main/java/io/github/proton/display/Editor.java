package io.github.proton.display;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
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
        return projection.characters().get(cursor).map(x -> cursor)
                .orElse(() -> left(projection, cursor))
                .orElse(() -> right(projection, cursor))
                .getOrElse(cursor.withColumn(0));
    }

    public static <T> Option<Projection.Char<T>> character(Projection<T> projection, TerminalPosition selected) {
        return projection.characters().get(selected);
    }

    public static <T> Option<TerminalPosition> left(Projection<T> projection, TerminalPosition cursor) {
        if (cursor.getColumn() < 0) return Option.none();
        TerminalPosition cursor1 = cursor.withRelativeColumn(-1);
        return projection.characters().get(cursor1)
                .map(c -> cursor1)
                .orElse(() -> left(projection, cursor1));
    }

    public static <T> Option<TerminalPosition> right(Projection<T> projection, TerminalPosition cursor) {
        if (cursor.getColumn() > projection.columns()) return Option.none();
        TerminalPosition cursor1 = cursor.withRelativeColumn(1);
        return projection.characters().get(cursor1)
                .map(c -> cursor1)
                .orElse(() -> right(projection, cursor1));
    }

    public Editor<T> update(KeyStroke keyStroke) {
        Projection<T> projection = projector.project(tree);
        TerminalPosition selected = selected(projection, cursor);
        if (projection.characters().isEmpty()) {
            return this;
        } else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            return new Editor<>(style, projector, tree, left(projection, selected).getOrElse(cursor));
        } else if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            return new Editor<>(style, projector, tree, right(projection, selected).getOrElse(cursor));
        } else if (keyStroke.getKeyType() == KeyType.ArrowUp) {
            return new Editor<>(style, projector, tree, cursor.withRow(Math.max(0, cursor.getRow() - 1)));
        } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
            return new Editor<>(style, projector, tree, cursor.withRow(Math.min(projection.rows() - 1, cursor.getRow() + 1)));
        } else if (keyStroke.getKeyType() == KeyType.Character) {
            return character(projection, selected).map(character -> {
                T t2 = character.insert(keyStroke.getCharacter()).getOrElse(tree);
                return new Editor<>(style, projector, t2, right(projector.project(t2), selected).getOrElse(cursor));
            }).getOrElse(this);
        } else if (keyStroke.getKeyType() == KeyType.Backspace) {
            if (selected.getColumn() == 0) {
                if (selected.getRow() == 0) return this;
                return new Editor<>(style, projector, tree, cursor.withRelativeRow(-1).withColumn(projection.columns()));
            }
            return character(projection, selected.withRelativeColumn(-1)).map(character -> {
                T t2 = character.delete().getOrElse(tree);
                return new Editor<>(style, projector, t2, left(projector.project(t2), selected).getOrElse(cursor));
            }).getOrElse(this);
        } else if (keyStroke.getKeyType() == KeyType.Enter) {
            return character(projection, selected).map(character -> {
                T t2 = character.submit().getOrElse(tree);
                TerminalPosition cursor = left(projector.project(t2), selected.withRelativeRow(1).withRelativeColumn(1))
                        .getOrElse(this.cursor);
                return new Editor<>(style, projector, t2, cursor);
            }).getOrElse(this);
        } else {
            return this;
        }
    }

    public void render(TerminalDisplay display) {
        display.background(style.base(' '));
        projector.project(tree).characters().forEach(c -> display.write(c._2.character(style), c._1));
        display.setCursor(selected(projector.project(tree), cursor));
    }
}
