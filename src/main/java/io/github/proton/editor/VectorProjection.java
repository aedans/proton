package io.github.proton.editor;

import io.vavr.collection.Vector;
import io.vavr.control.Option;

import java.util.function.Predicate;

public record VectorProjection<T>(Vector<T>vector,
                                  Projector<T>projector,
                                  Projection<Vector<T>>separator,
                                  T terminator,
                                  Predicate<T>isEmpty) implements Projection.Delegate<Vector<T>> {
    @Override
    public Projection<Vector<T>> delegate() {
        return vector
            .zipWithIndex((e, i) -> projector.project(e)
                .mapChars(c -> c.modify(
                    character -> c.insert(character).map(t -> vector.update(i, t)),
                    () -> c.delete().map(t -> isEmpty.test(t) ? vector.removeAt(i) : vector.update(i, t)))))
            .append(projector.project(terminator).map(vector::append).mapChars(c -> c
                .withDelete(() -> Option.some(vector))
                .mapStyle(style -> style.of("comment.ignored"))))
            .intersperse(separator)
            .reduceOption(Projection::combine)
            .getOrElse(Projection.empty());
    }
}
