package io.github.proton.editor;

import io.vavr.collection.Vector;
import io.vavr.control.Option;

public record VectorProjection<T>(Vector<T>vector,
                                  Projector<T>projector,
                                  Projection<Vector<T>>separator,
                                  T elem) implements Projection<Vector<T>> {
    @Override
    public Result<Vector<T>> project(int width, boolean fit, int space, int position, int indent) {
        return vector.zipWithIndex((e, i) -> projector.project(e)
                .mapChars(c -> new Char<Vector<T>>() {
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
