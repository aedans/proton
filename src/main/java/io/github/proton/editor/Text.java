package io.github.proton.editor;

import io.vavr.collection.Vector;

public record Text(Vector<Character> chars) {
    public Text(String string) {
        this(Vector.ofAll(string.toCharArray()));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        chars.forEach(builder::append);
        return builder.toString();
    }
}
