package io.github.proton.plugins.java.tree;

import io.github.proton.editor.Text;
import io.vavr.collection.Vector;

public record JavaIdentifier(Vector<Character> chars) {
    public JavaIdentifier(String name) {
        this(new Text(name));
    }

    public JavaIdentifier(Text text) {
        this(text.chars());
    }

    public static boolean isValid(char c) {
        return Character.isAlphabetic(c) || Character.isDigit(c);
    }

    public boolean isEmpty() {
        return chars.isEmpty();
    }

    @Override
    public String toString() {
        return new Text(chars).toString();
    }
}
