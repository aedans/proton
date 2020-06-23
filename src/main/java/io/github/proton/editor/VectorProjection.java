package io.github.proton.editor;

import io.vavr.collection.Vector;
import io.vavr.control.Option;

import java.util.function.Predicate;

public record VectorProjection<T>(Vector<T>vector,
                                  Projector<T>projector,
                                  Projection<Vector<T>>separator,
                                  T elem,
                                  Predicate<T>isEmpty) implements Projection.Delegate<Vector<T>> {
    @Override
    public Projection<Vector<T>> delegate() {
        return vector.zipWithIndex((e, i) -> projector.project(e)
            .mapChars(c -> new Char<Vector<T>>() {
                @Override
                public boolean decorative() {
                    return c.decorative();
                }

                @Override
                public boolean mergeable() {
                    return c.mergeable();
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
                    return c.delete().map(t -> isEmpty.test(t) ? vector.removeAt(i) : vector.update(i, t));
                }
            }))
            .append(projector.project(elem).map(vector::append).mapChars(c -> new Char<Vector<T>>() {
                @Override
                public boolean decorative() {
                    return c.decorative();
                }

                @Override
                public boolean mergeable() {
                    return c.mergeable();
                }

                @Override
                public StyledCharacter character(Style style) {
                    return c.character(style.of("comment.invisible"));
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
            .getOrElse(Projection.empty());
    }
}
