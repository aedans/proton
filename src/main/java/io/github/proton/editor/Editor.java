package io.github.proton.editor;

import io.vavr.collection.Vector;
import io.vavr.control.Option;

import java.awt.event.KeyEvent;

public final class Editor<T> {
    public final Style style;
    public final Projector<T> projector;
    public final int width;
    public final T tree;
    public final int index;
    public final int col;
    public final Vector<Char<T>> chars;

    public Editor(Style style, Projector<T> projector, int width, T tree, int index) {
        this(style, projector, width, tree, index, Option.none());
    }

    public Editor(Style style, Projector<T> projector, int width, T tree, int index, Option<Integer> col) {
        this.style = style;
        this.projector = projector;
        this.width = width;
        this.tree = tree;
        this.index = index;
        this.chars = projector.project(tree).chars(width);
        this.col = col.getOrElse(() -> position(chars, index).col());
    }

    public static <T> int selectedLength(Vector<Char<T>> chars) {
        return chars.filter(Char::edit).length();
    }

    public static <T> int selectedIndex(Vector<Char<T>> chars, int index) {
        return chars.zipWithIndex().filter(x -> x._1.edit()).get(index)._2;
    }

    public static <T> Char<T> selected(Vector<Char<T>> chars, int index) {
        return chars.get(selectedIndex(chars, index));
    }

    public static <T> Position position(Vector<Char<T>> chars, int index) {
        var i = selectedIndex(chars, index);
        var row = chars.take(i).filter(x -> x.character() == '\n').length();
        var col = chars.take(i).reverse().takeUntil(x -> x.character() == '\n').size();
        return new Position(row, col);
    }

    public static <T> int up(Vector<Char<T>> chars, int index, int col) {
        var target = position(chars, index).withRelativeRow(-1).withCol(col);
        while (true) {
            index--;
            if (index < 0)
                return 0;
            var position = position(chars, index);
            if (position.row() < target.row())
                return index + 1;
            if (position.row() == target.row() && position.col() <= target.col())
                return index;
        }
    }

    public static <T> int down(Vector<Char<T>> chars, int index, int col) {
        var target = position(chars, index).withRelativeRow(1).withCol(col);
        var size = selectedLength(chars) - 1;
        while (true) {
            index++;
            if (index > size)
                return size;
            var position = position(chars, index);
            if (position.row() > target.row())
                return index - 1;
            if (position.row() == target.row() && position.col() >= target.col())
                return index;
        }
    }

    public Editor<T> update(KeyEvent key) {
        if (chars.isEmpty())
            return this;
        return switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT -> new Editor<>(style, projector, width, tree, index - 1);
            case KeyEvent.VK_RIGHT -> new Editor<>(style, projector, width, tree, index + 1);
            case KeyEvent.VK_UP -> new Editor<>(style, projector, width, tree, up(chars, index, col), Option.some(col));
            case KeyEvent.VK_DOWN -> new Editor<>(style, projector, width, tree, down(chars, index, col), Option.some(col));
            case KeyEvent.VK_DELETE -> delete();
            case KeyEvent.VK_BACK_SPACE -> backspace();
            case KeyEvent.VK_ENTER -> enter();
            case KeyEvent.VK_SHIFT, KeyEvent.VK_ALT, KeyEvent.VK_CAPS_LOCK, KeyEvent.VK_ESCAPE, KeyEvent.VK_CONTROL -> this;
            default -> insert(key.getKeyChar());
        };
    }

    public Editor<T> delete() {
        return new Editor<>(style, projector, width, selected(chars, index).delete().getOrElse(tree), index);
    }

    public Editor<T> backspace() {
        return new Editor<>(style, projector, width, selected(chars, index - 1).delete().getOrElse(tree), index - 1);
    }

    public Editor<T> enter() {
        var t = selected(chars, index).insert('\n').getOrElse(tree);
        return new Editor<>(style, projector, width, t, down(projector.project(t).chars(width), index, col));
    }

    public Editor<T> insert(char c) {
        return new Editor<>(style, projector, width, selected(chars, index).insert(c).getOrElse(tree), index + 1);
    }
}
