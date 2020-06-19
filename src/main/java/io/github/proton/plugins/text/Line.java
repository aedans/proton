/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.text;

import io.vavr.collection.Vector;
import java.util.Objects;

public final class Line {
    public final Vector<Character> chars;

    public Line(Vector<Character> chars) {
        this.chars = chars;
    }

    public Line(String string) {
        this(Vector.ofAll(string.toCharArray()));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        chars.forEach(builder::append);
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(chars, line.chars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chars);
    }
}
