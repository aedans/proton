/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.tree;

import io.github.proton.plugins.text.Line;
import io.vavr.collection.Vector;

import java.util.Objects;

public final class JavaIdentifier {
    public final Vector<Character> chars;

    public JavaIdentifier(String name) {
        this(new Line(name));
    }

    public JavaIdentifier(Line line) {
        this(line.chars);
    }

    public JavaIdentifier(Vector<Character> chars) {
        this.chars = chars;
    }

    public static boolean isValid(char c) {
        return Character.isAlphabetic(c) || Character.isDigit(c);
    }

    @Override
    public String toString() {
        return new Line(chars).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaIdentifier that = (JavaIdentifier) o;
        return Objects.equals(chars, that.chars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chars);
    }
}
