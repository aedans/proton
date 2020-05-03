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

public final class VectorProjection<T> extends Projection<Vector<T>> {
    public VectorProjection(Vector<T> vector, Projector<T> projector, T elem) {
        super(characters(vector, projector, elem));
    }

    private static <T> Map<TerminalPosition, Char<Vector<T>>> characters(
            Vector<T> vector, Projector<T> projector, T elem) {
        return vector.<Projection<Vector<T>>>zipWithIndex((e, i) ->
                        new Projection<>(projector.project(e).characters.mapValues(c -> new Char<Vector<T>>() {
                            @Override
                            public boolean decorative() {
                                return c.decorative();
                            }

                            @Override
                            public TextCharacter character(Style style) {
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

                            @Override
                            public Option<Vector<T>> submit() {
                                return c.submit().map(t -> vector.update(i, t));
                            }
                        })))
                .append(projector.project(elem).map(vector::append).mapChars(c -> new Char<Vector<T>>() {
                    @Override
                    public boolean decorative() {
                        return c.decorative();
                    }

                    @Override
                    public TextCharacter character(Style style) {
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

                    @Override
                    public Option<Vector<T>> submit() {
                        return c.submit();
                    }
                }))
                .reduceOption(Projection::combineVertical)
                .map(x -> x.characters)
                .getOrElse(HashMap.empty());
    }
}
