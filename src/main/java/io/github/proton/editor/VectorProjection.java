package io.github.proton.editor;

import io.vavr.collection.Vector;
import io.vavr.control.Option;

import java.util.function.*;

public record VectorProjection<T>(Vector<T>vector,
                                  Function<T, Projection<T>>projector,
                                  T empty,
                                  Projection<Vector<T>>separator,
                                  Predicate<T>isEmpty,
                                  Predicate<Character>insert) implements Projection.Delegate<Vector<T>> {
    @Override
    public Projection<Vector<T>> delegate() {
        var vector = this.vector.isEmpty() ? Vector.of(empty) : this.vector;
        return vector
            .zipWithIndex((e, i) -> projector.apply(e)
                .mapChars(chars -> {
                    var chars1 = chars.map(c -> c.modify(
                        character -> c.insert(character).map(t -> vector.update(i, t)),
                        () -> c.delete().map(t -> vector.update(i, t))));
                    var headIndex = chars1.zipWithIndex().find(x -> x._1.edit()).get()._2;
                    var lastIndex = chars1.zipWithIndex().findLast(x -> x._1.edit()).get()._2;
                    return chars1
                        .update(headIndex, c -> c.modify(
                            character -> c.insert(character).orElse(() -> insert.test(character)
                                ? Option.some(vector.insert(i, empty))
                                : Option.none()),
                            () -> c.delete().orElse(() -> i < vector.size() && isEmpty.test(vector.get(i))
                                ? Option.some(vector.removeAt(i))
                                : Option.none())))
                        .update(lastIndex, c -> c.modify(
                            character -> c.insert(character).orElse(() -> insert.test(character)
                                ? Option.some(vector.insert(i + 1, empty))
                                : Option.none()),
                            () -> c.delete().orElse(() -> i + 1 < vector.size() && isEmpty.test(vector.get(i + 1))
                                ? Option.some(vector.removeAt(i + 1))
                                : Option.none())));
                }))
            .intersperse(separator)
            .reduce(Projection::combine)
            .map(x -> x.size() == 1 && isEmpty.test(x.head()) ? Vector.empty() : x);
    }
}
