package io.github.proton.editor;

import io.vavr.collection.Vector;
import io.vavr.control.Option;

import java.awt.event.KeyEvent;

public final class Editor<T> {
    public final Style style;
    public final Projector<T> projector;
    public final int width;
    public final T tree;
    public final int position;
    public final int col;

    public Editor(Style style, Projector<T> projector, int width, T tree, int position) {
        this(style, projector, width, tree, position, Option.none());
    }

    public Editor(Style style, Projector<T> projector, int width, T tree, int position, Option<Integer> col) {
        this.style = style;
        this.projector = projector;
        this.width = width;
        this.tree = tree;
        this.position = position;
        this.col = col.getOrElse(() ->
            chars().take(selectedIndex()).reverse().takeUntil(x -> x.character() == '\n').size());
    }

    public Vector<Char<T>> chars() {
        return projector.project(tree).chars(width);
    }

    public int selectedIndex() {
        return chars().zipWithIndex().filter(x -> !x._1.decorative()).get(position)._2;
    }

    public Char<T> selected() {
        return chars().get(selectedIndex());
    }

    public int up() {
        var chars = chars();
        var i = selectedIndex();
        var pos = position;
        if (!chars.get(i).decorative())
            pos--;
        i--;
        for (; chars.get(i).character() != '\n'; i--) {
            if (!chars.get(i).decorative())
                pos--;
        }
        if (!chars.get(i).decorative())
            pos--;
        i--;
        for (; chars.get(i).character() != '\n'; i--) {
            if (!chars.get(i).decorative())
                pos--;
        }
        i++;
        for (int j = 0; j < col; j++) {
            if (chars.get(i + j + 1).character() == '\n')
                break;
            if (!chars.get(i + j + 1).decorative())
                pos++;
        }
        return Math.min(pos, position - 1);
    }

    public int down() {
        var chars = chars();
        var i = selectedIndex();
        var pos = position;
        for (; chars.get(i).character() != '\n'; i++) {
            if (!chars.get(i).decorative())
                pos++;
        }
        if (!chars.get(i).decorative())
            pos++;
        for (int j = 0; j < col; j++) {
            if (chars.get(i + j + 1).character() == '\n')
                break;
            if (!chars.get(i + j + 1).decorative())
                pos++;
        }
        return Math.max(pos, position + 1);
    }

    public Editor<T> update(KeyEvent key) {
        var characters = projector.project(tree).chars(width);
        if (characters.isEmpty())
            return this;
        return switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT -> new Editor<>(style, projector, width, tree, position - 1);
            case KeyEvent.VK_RIGHT -> new Editor<>(style, projector, width, tree, position + 1);
            case KeyEvent.VK_UP -> new Editor<>(style, projector, width, tree, up(), Option.some(col));
            case KeyEvent.VK_DOWN -> new Editor<>(style, projector, width, tree, down(), Option.some(col));
            case KeyEvent.VK_DELETE -> delete();
            case KeyEvent.VK_BACK_SPACE -> backspace();
            case KeyEvent.VK_ENTER -> enter();
            case KeyEvent.VK_SHIFT, KeyEvent.VK_ALT, KeyEvent.VK_CAPS_LOCK, KeyEvent.VK_ESCAPE -> this;
            default -> insert(key.getKeyChar());
        };
    }

    public Editor<T> delete() {
        return new Editor<>(style, projector, width, selected().delete().getOrElse(tree), position);
    }

    public Editor<T> backspace() {
        var previous = new Editor<>(style, projector, width, tree, position - 1).selected();
        return new Editor<>(style, projector, width, previous.delete().getOrElse(tree), position - 1);
    }

    public Editor<T> enter() {
        return new Editor<>(style, projector, width, selected().insert('\n').getOrElse(tree), down());
    }

    public Editor<T> insert(char c) {
        return new Editor<>(style, projector, width, selected().insert(c).getOrElse(tree), position + 1);
    }
}
