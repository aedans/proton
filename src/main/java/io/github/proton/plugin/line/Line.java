package io.github.proton.plugin.line;

import io.vavr.collection.Vector;

public final class Line {
    public final Vector<Character> characters;

    public Line(Vector<Character> characters) {
        this.characters = characters;
    }

    public Line(String characters) {
        this(Vector.ofAll(characters.toCharArray()));
    }
}
