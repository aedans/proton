package io.github.proton.editor;

import io.vavr.collection.Vector;
import io.vavr.control.Option;

import java.util.function.*;

public record InsertProjection<T>(Vector<T>vector,
                                  Function<T, Projection<T>>projector,
                                  Projection<Vector<T>>separator,
                                  T terminator,
                                  Predicate<T>isEmpty,
                                  Predicate<Character>insert) implements Projection.Delegate<Vector<T>> {
    @Override
    public Projection<Vector<T>> delegate() {
        return vector
            .zipWithIndex((e, i) -> projector.apply(e)
                .mapChars(c -> c.modify(
                    character -> c.insert(character).map(t -> vector.update(i, t))
                        .orElse(() -> insert.test(character)
                            ? Option.some(vector.insert(i + 1, terminator))
                            : Option.none()),
                    () -> c.delete()
                        .map(t -> vector.update(i, t))
                        .orElse(() -> i + 1 < vector.size()
                            ? Option.some(vector.removeAt(i + 1))
                            : Option.none()))))
            .intersperse(separator)
            .reduceOption(Projection::combine)
            .getOrElse(projector.apply(terminator).map(vector::append).mapChars(c -> c
                .withDelete(Option::none)))
            .map(x -> x.headOption()
                .map(h -> isEmpty().test(h) ? Vector.<T>empty() : x)
                .getOrElse(x));
    }
}
