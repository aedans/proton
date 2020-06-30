package io.github.proton.editor;

import io.vavr.collection.Vector;
import io.vavr.control.Option;

import java.util.function.*;

public record AppendProjection<T>(Vector<T>vector,
                                  Function<T, Projection<T>>projector,
                                  T terminator,
                                  Predicate<T>isEmpty) implements Projection.Delegate<Vector<T>> {
    @Override
    public Projection<Vector<T>> delegate() {
        return vector
            .zipWithIndex((e, i) -> projector.apply(e)
                .mapChars(c -> c.modify(
                    character -> c.insert(character).map(t -> vector.update(i, t)),
                    () -> c.delete().map(t -> isEmpty.test(t) ? vector.removeAt(i) : vector.update(i, t)))))
            .append(projector.apply(terminator).map(vector::append).mapChars(c -> c
                .withDelete(Option::none)
                .mapStyle(style -> style.of("comment.ignored"))))
            .reduceOption(Projection::combine)
            .getOrElse(Projection.empty());
    }
}
