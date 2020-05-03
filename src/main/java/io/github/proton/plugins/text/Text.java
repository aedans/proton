/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.text;

import io.vavr.collection.Vector;

import java.util.Objects;

public final class Text {
    public final Vector<Line> lines;

    public Text(Vector<Line> lines) {
        this.lines = lines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Text text = (Text) o;
        return Objects.equals(lines, text.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines);
    }
}
