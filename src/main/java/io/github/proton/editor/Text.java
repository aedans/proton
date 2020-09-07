package io.github.proton.editor;

import io.vavr.collection.Vector;

public record Text(Vector<Character> chars) implements Tree<Text> {
    public Text(String string) {
        this(Vector.ofAll(string.toCharArray()));
    }

    @Override
    public boolean isEmpty() {
        return chars.isEmpty();
    }

    @Override
    public Projection<Text> project() {
        return TextProjection.text(this, "");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        chars.forEach(builder::append);
        return builder.toString();
    }
}
