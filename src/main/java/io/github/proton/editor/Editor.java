package io.github.proton.editor;

import io.vavr.collection.Vector;
import io.vavr.control.Option;

public final class Editor<T extends Tree<T>> {
    public final Vector<Char<T>> chars;
    public final int width;
    public final int dot;
    public final int mark;
    public final int col;

    public Editor(T tree)  {
        this(tree, 0, 0, 0);
    }

    public Editor(T tree, int width, int dot, int mark) {
        this(tree.project().chars(width), width, dot, mark, Option.none());
    }

    public Editor(Vector<Char<T>> chars, int width, int dot, int mark) {
        this(chars, width, dot, mark, Option.none());
    }

    public Editor(Vector<Char<T>> chars, int width, int dot, int mark, Option<Integer> col) {
        this.chars = chars;
        this.width = width;
        this.dot = constrain(chars, dot);
        this.mark = constrain(chars, mark);
        this.col = col.getOrElse(() -> position(chars, dot).col());
    }

    private static <T> int selectedLength(Vector<Char<T>> chars) {
        return chars.filter(Char::edit).length();
    }

    public static <T> int selectedIndex(Vector<Char<T>> chars, int index) {
        var filter = chars.zipWithIndex().filter(x -> x._1.edit());
        return index >= filter.length() ? filter.length() : filter.get(index)._2;
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

    private static <T extends Tree<T>> Vector<Char<T>> deleteRange(Vector<Char<T>> chars, int width, int start, int end) {
        for (int i = end - 1; i >= start; --i) {
            chars = selected(chars, i).delete().map(x -> x.project().chars(width)).getOrElse(chars);
        }
        return chars;
    }

    private static <T extends Tree<T>> Vector<Char<T>> delete(Vector<Char<T>> chars, int width, int dot, int mark) {
        if (dot < mark) {
            return deleteRange(chars, width, dot, mark);
        } else if (dot > mark) {
            return deleteRange(chars, width, mark, dot);
        } else {
            return chars;
        }
    }

    public Editor<T> left() {
        var left = dot == mark ? dot - 1 : Math.min(dot, mark);
        return new Editor<>(chars, width, left, left);
    }

    public Editor<T> right() {
        var right = dot == mark ? dot + 1 : Math.max(dot, mark);
        return new Editor<>(chars, width, right, right);
    }

    public Editor<T> up() {
        var up = up(chars, dot, col);
        return new Editor<>(chars, width, up, up, Option.some(col));
    }

    public Editor<T> down() {
        var down = down(chars, dot, col);
        return new Editor<>(chars, width, down, down, Option.some(col));
    }

    public Editor<T> select(int dot, int mark) {
        if (dot >= chars.length() || mark >= chars.length()) {
            return this;
        } else {
            return new Editor<>(chars, width,
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
                return selected(chars, left).delete()
                    .map(chars -> new Editor<>(chars, width, left, left))
                    .getOrElse(new Editor<>(chars, width, left, left));
            }
        } else {
            var cs = delete(chars, width, dot, mark);
            var left = Math.min(dot, mark);
            return new Editor<>(cs, width, left, left);
        }
    }

    public Editor<T> delete() {
        if (dot == mark) {
            var here = constrain(chars, dot);
            return selected(chars, dot).delete()
                .map(chars -> new Editor<>(chars, width, here, here))
                .getOrElse(new Editor<>(chars, width, here, here));
        } else {
            var cs = delete(chars, width, dot, mark);
            var left = Math.min(dot, mark);
            return new Editor<>(cs, width, left, left);
        }
    }

    public Editor<T> enter() {
        var inserted = insert('\n');
        var down = down(inserted.chars, Math.min(dot, mark), 0);
        return new Editor<>(inserted.chars, width, down, down);
    }

    public Editor<T> insert(char c) {
        if (dot == mark) {
            var cs = selected(chars, dot).insert(c).map(x -> x.project().chars(width)).getOrElse(chars);
            var right = dot + 1;
            return new Editor<>(cs, width, right, right);
        } else {
            var cs = delete(chars, width, dot, mark);
            cs = selected(cs, Math.min(dot, mark)).insert(c).map(x -> x.project().chars(width)).getOrElse(cs);
            var right = Math.min(dot, mark) + 1;
            return new Editor<>(cs, width, right, right);
        }
    }
}
