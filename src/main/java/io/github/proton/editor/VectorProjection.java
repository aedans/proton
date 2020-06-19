/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.editor;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

public final class VectorProjection<T> implements Projection<Vector<T>> {
    private final Vector<T> vector;
    private final Projector<T> projector;
    private final T elem;

    public VectorProjection(Vector<T> vector, Projector<T> projector, T elem) {
        this.vector = vector;
        this.projector = projector;
        this.elem = elem;
    }

    @Override
    public Map<Position, Char<Vector<T>>> characters() {
        return vector.<Projection<Vector<T>>>zipWithIndex((e, i) ->
                        Projection.of(projector.project(e).characters().mapValues(c -> new Char<Vector<T>>() {
                            @Override
                            public boolean decorative() {
                                return c.decorative();
                            }

                            @Override
                            public StyledCharacter character(Style style) {
                                return c.character(style);
                            }

                            @Override
                            public Option<Vector<T>> insert(char character) {
                                return c.insert(character).map(t -> vector.update(i, t));
                            }

                            @Override
                            public Option<Vector<T>> delete() {
                                return c.delete().map(t -> {
                                    if (t.equals(elem)) {
                                        return vector.removeAt(i);
                                    } else {
                                        return vector.update(i, t);
                                    }
                                });
                            }
                        })))
                .append(projector.project(elem).map(vector::append).mapChars(c -> new Char<Vector<T>>() {
                    @Override
                    public boolean decorative() {
                        return c.decorative();
                    }

                    @Override
                    public StyledCharacter character(Style style) {
                        return c.character(style.of("comment.ignored"));
                    }

                    @Override
                    public Option<Vector<T>> insert(char character) {
                        return c.insert(character);
                    }

                    @Override
                    public Option<Vector<T>> delete() {
                        return Option.some(vector);
                    }
                }))
                .reduceOption(Projection::combineVertical)
                .map(Projection::characters)
                .getOrElse(HashMap.empty());
    }
}
