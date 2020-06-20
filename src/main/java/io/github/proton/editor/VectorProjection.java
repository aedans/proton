/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.editor;

import io.vavr.collection.Vector;
import io.vavr.control.Option;

public final class VectorProjection<T> implements Projection<Vector<T>> {
    private final Vector<T> vector;
    private final Projector<T> projector;
    private final Projection<Vector<T>> separator;
    private final T elem;

    public VectorProjection(Vector<T> vector, Projector<T> projector, Projection<Vector<T>> separator, T elem) {
        this.vector = vector;
        this.projector = projector;
        this.separator = separator;
        this.elem = elem;
    }

    @Override
    public Result<Vector<T>> project(int width, boolean fit, int space, int position, int indent) {
        return vector.zipWithIndex((e, i) -> projector.project(e).mapChars(c1 -> new Char<Vector<T>>() {
                    @Override
                    public boolean decorative() {
                        return c1.decorative();
                    }

                    @Override
                    public StyledCharacter character(Style style) {
                        return c1.character(style);
                    }

                    @Override
                    public Option<Vector<T>> insert(char character) {
                        return c1.insert(character).map(t -> vector.update(i, t));
                    }

                    @Override
                    public Option<Vector<T>> delete() {
                        return c1.delete().map(t -> {
                            if (t.equals(elem)) {
                                return vector.removeAt(i);
                            } else {
                                return vector.update(i, t);
                            }
                        });
                    }
                }))
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
                .intersperse(separator)
                .reduceOption(Projection::combine)
                .map(x -> x.project(width, fit, space, position, indent))
                .getOrElse(new Result<>(space, position, Vector.empty()));
    }
}
