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

    public static TerminalPosition selected(Projection<?> projection, TerminalPosition cursor) {
        return projection.characters().get(cursor).map(x -> cursor)
                .orElse(() -> left(projection, cursor))
                .orElse(() -> right(projection, cursor))
                .getOrElseThrow(IndexOutOfBoundsException::new);
    }

    public static <T> Projection.Char<T> character(Projection<T> projection, TerminalPosition cursor) {
        return projection.characters().get(selected(projection, cursor))
                .getOrElseThrow(IndexOutOfBoundsException::new);
    }

    public static Option<TerminalPosition> left(Projection<?> projection, TerminalPosition cursor) {
        if (cursor.getColumn() < 0) return Option.none();
        TerminalPosition cursor1 = cursor.withRelativeColumn(-1);
        return projection.characters().get(cursor1)
                .map(c -> cursor1)
                .orElse(() -> left(projection, cursor1));
    }

    public static Option<TerminalPosition> right(Projection<?> projection, TerminalPosition cursor) {
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
            T t2 = character(projection, selected).insert(keyStroke.getCharacter());
            return new Editor<>(style, projector, t2, right(projector.project(t2), selected).getOrElse(cursor));
        } else if (keyStroke.getKeyType() == KeyType.Backspace) {
            if (selected.getColumn() == 0) return this;
            T t2 = character(projection, selected.withRelativeColumn(-1)).delete();
            return new Editor<>(style, projector, t2, left(projector.project(t2), selected).getOrElse(cursor));
        } else if (keyStroke.getKeyType() == KeyType.Enter) {
            T t2 = character(projection, selected).submit();
            return new Editor<>(style, projector, t2, cursor);
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
