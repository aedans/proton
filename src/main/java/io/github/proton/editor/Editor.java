package io.github.proton.editor;

import io.vavr.collection.Vector;
import io.vavr.control.Option;

public final class Editor<T> {
    public final Projector<T> projector;
    public final int width;
    public final T tree;
    public final int dot;
    public final int mark;
    public final int col;
    public final Vector<Char<T>> chars;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Editor(T tree) {
        this(Projector.get((Class) tree.getClass()), 0, tree, 0, 0);
    }

    public Editor(Projector<T> projector, int width, T tree, int dot, int mark) {
        this(projector, width, tree, dot, mark, Option.none());
    }

    public Editor(Projector<T> projector, int width, T tree, int dot, int mark, Option<Integer> col) {
        this.projector = projector;
        this.width = width;
        this.tree = tree;
        this.chars = projector.project(tree).chars(width);
        this.dot = constrain(chars, dot);
        this.mark = constrain(chars, mark);
        this.col = col.getOrElse(() -> position(chars, dot).col());
    }

    private static <T> int selectedLength(Vector<Char<T>> chars) {
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

    private static <T> Char<T> selected(Vector<Char<T>> chars, int index) {
        return chars.get(selectedIndex(chars, index));
    }

    private static <T> Position position(Vector<Char<T>> chars, int index) {
        var i = selectedIndex(chars, index);
        var row = chars.take(i).filter(x -> x.character() == '\n').length();
        var col = chars.take(i).reverse().takeUntil(x -> x.character() == '\n').size();
        return new Position(row, col);
    }

    private static <T> int constrain(Vector<Char<T>> chars, int index) {
        var size = selectedLength(chars) - 1;
        return index <= 0 ? 0 : Math.min(index, size);
    }

    private static <T> int up(Vector<Char<T>> chars, int index, int col) {
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

    private static <T> int down(Vector<Char<T>> chars, int index, int col) {
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

    private static <T> T deleteRange(T tree, Projector<T> projector, int width, int start, int end) {
        for (int i = end - 1; i >= start; --i) {
            var chars = projector.project(tree).chars(width);
            tree = selected(chars, i).delete().getOrElse(tree);
        }
        return tree;
    }

    private static <T> T delete(T tree, Projector<T> projector, int width, int dot, int mark) {
        if (dot < mark) {
            return deleteRange(tree, projector, width, dot, mark);
        } else if (dot > mark) {
            return deleteRange(tree, projector, width, mark, dot);
        } else {
            return tree;
        }
    }

    public Editor<T> left() {
        int left;
        if (dot == mark) {
            left = dot - 1;
        } else {
            left = Math.min(dot, mark);
        }
        return new Editor<>(projector, width, tree, left, left);
    }

    public Editor<T> right() {
        int right;
        if (dot == mark) {
            right = dot + 1;
        } else {
            right = Math.max(dot, mark);
        }
        return new Editor<>(projector, width, tree, right, right);
    }

    public Editor<T> up() {
        int up = up(chars, dot, col);
        return new Editor<>(projector, width, tree, up, up, Option.some(col));
    }

    public Editor<T> down() {
        int down = down(chars, dot, col);
        return new Editor<>(projector, width, tree, down, down, Option.some(col));
    }

    public Editor<T> select(int dot, int mark) {
        if (dot >= chars.length() || mark >= chars.length()) {
            return this;
        } else {
            return new Editor<>(projector, width, tree,
                chars.take(dot).filter(Char::edit).length(),
                chars.take(mark).filter(Char::edit).length());
        }
    }

    public Editor<T> backspace() {
        if (dot == mark) {
            if (dot == 0) {
                return this;
            } else {
                var left = dot - 1;
                return new Editor<>(projector, width, selected(chars, left).delete().getOrElse(tree), left, left);
            }
        } else {
            var t = delete(tree, projector, width, dot, mark);
            var left = Math.min(dot, mark);
            return new Editor<>(projector, width, t, left, left);
        }
    }

    public Editor<T> delete() {
        if (dot == mark) {
            var here = constrain(chars, dot);
            return new Editor<>(projector, width, selected(chars, dot).delete().getOrElse(tree), here, here);
        } else {
            var t = delete(tree, projector, width, dot, mark);
            var left = Math.min(dot, mark);
            return new Editor<>(projector, width, t, left, left);
        }
    }

    public Editor<T> enter() {
        var inserted = insert('\n');
        var down = down(inserted.chars, Math.min(dot, mark), 0);
        return new Editor<>(projector, width, inserted.tree, down, down);
    }

    public Editor<T> insert(char c) {
        if (dot == mark) {
            var t = selected(chars, dot).insert(c).getOrElse(tree);
            var right = dot + 1;
            return new Editor<>(projector, width, t, right, right);
        } else {
            var t = delete(tree, projector, width, dot, mark);
            t = selected(projector.project(t).chars(width), Math.min(dot, mark)).insert(c).getOrElse(t);
            var right = Math.min(dot, mark) + 1;
            return new Editor<>(projector, width, t, right, right);
        }
    }
}
