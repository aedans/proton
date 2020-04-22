/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.display;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

public final class VectorProjection<T> implements Projection<Vector<T>> {
    private final Vector<T> vector;
    private final Projector<T> projector;
    private final Option<T> elem;

    public VectorProjection(Vector<T> vector, Projector<T> projector) {
        this(vector, projector, Option.none());
    }

    public VectorProjection(Vector<T> vector, Projector<T> projector, T elem) {
        this(vector, projector, Option.of(elem));
    }

    public VectorProjection(Vector<T> vector, Projector<T> projector, Option<T> elem) {
        this.vector = vector;
        this.projector = projector;
        this.elem = elem;
    }

    @Override
    public Map<TerminalPosition, Char<Vector<T>>> characters() {
        return vector.<Projection<Vector<T>>>zipWithIndex(
                        (elem, i) -> () -> projector.project(elem).characters().mapValues(c -> new Char<Vector<T>>() {
                            @Override
                            public boolean decorative() {
                                return c.decorative();
                            }

                            @Override
                            public TextCharacter character(Style style) {
                                return c.character(style);
                            }

                            @Override
                            public Option<Vector<T>> insert(char w) {
                                return c.insert(w).map(t -> vector.update(i, t));
                            }

                            @Override
                            public Option<Vector<T>> delete() {
                                return c.delete().map(t -> vector.update(i, t)).orElse(() ->
                                        Option.of(vector.removeAt(i)));
                            }

                            @Override
                            public Option<Vector<T>> submit() {
                                return c.submit().map(t -> vector.update(i, t)).orElse(() ->
                                        VectorProjection.this.elem.map(e -> vector.insert(i + 1, e)));
                            }
                        }))
                .reduceOption(Projection::combineVertical)
                .map(Projection::characters)
                .getOrElse(HashMap.empty());
    }
}
