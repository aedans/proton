package io.github.proton.plugins.text;

import io.vavr.collection.Vector;

public final class Line {
    public final Vector<Character> chars;

    public Line(Vector<Character> chars) {
        this.chars = chars;
    }

    public Line(String string) {
        this(Vector.ofAll(string.toCharArray()));
    }
}
