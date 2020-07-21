package io.github.proton.editor;

import io.vavr.collection.Vector;
import io.vavr.control.Option;

public final class Editor<T> {
    public final Projector<T> projector;
    public final int width;
    public final T tree;
    public final int index;
    public final int col;
    public final Vector<Char<T>> chars;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Editor(T tree) {
        this(Projector.get((Class) tree.getClass()), 0, tree, 0);
    }

    public Editor(Projector<T> projector, int width, T tree, int index) {
        this(projector, width, tree, index, Option.none());
    }

    public Editor(Projector<T> projector, int width, T tree, int index, Option<Integer> col) {
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
        var filter = chars.zipWithIndex().filter(x -> x._1.edit());
        if (index >= filter.length()) {
            return filter.length();
        } else {
            return filter.get(index)._2;
        }
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

    public static int left(int index) {
        return index <= 1 ? 0 : index - 1;
    }

    public static <T> int right(Vector<Char<T>> chars, int index) {
        var size = selectedLength(chars) - 1;
        return index >= size ? size : index + 1;
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

    public Editor<T> left() {
        return new Editor<>(projector, width, tree, left(index));
    }

    public Editor<T> right() {
        return new Editor<>(projector, width, tree, right(chars, index));
    }

    public Editor<T> up() {
        return new Editor<>(projector, width, tree, up(chars, index, col), Option.some(col));
    }

    public Editor<T> down() {
        return new Editor<>(projector, width, tree, down(chars, index, col), Option.some(col));
    }

    public Editor<T> select(int dot) {
        if (dot >= chars.length()) {
            return this;
        } else {
            return new Editor<>(projector, width, tree, chars.take(dot).filter(Char::edit).length());
        }
    }

    public Editor<T> delete() {
        return new Editor<>(projector, width, selected(chars, index).delete().getOrElse(tree), index);
    }

    public Editor<T> backspace() {
        if (index == 0) {
            return this;
        } else {
            return new Editor<>(projector, width, selected(chars, index - 1).delete().getOrElse(tree), index - 1);
        }
    }

    public Editor<T> enter() {
        var t = selected(chars, index).insert('\n').getOrElse(tree);
        return new Editor<>(projector, width, t, down(projector.project(t).chars(width), index, 0));
    }

    public Editor<T> insert(char c) {
        return new Editor<>(projector, width, selected(chars, index).insert(c).getOrElse(tree), index + 1);
    }
}
