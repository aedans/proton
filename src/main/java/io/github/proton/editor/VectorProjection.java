package io.github.proton.editor;

import io.vavr.collection.Vector;
import io.vavr.control.Option;

import java.util.function.Predicate;

public record VectorProjection<T extends Tree<T>>(Vector<T> vector,
                                                  T empty,
                                                  Projection<?> separator,
                                                  Predicate<T> isEmpty,
                                                  Predicate<Character> insert) implements Projection.Delegate<Vector<T>> {
    public Projection<Vector<T>> withAppend() {
        return withAppend(separator);
    }

    public Projection<Vector<T>> withAppend(Projection<?> separator) {
        return this
            .combine(separator.of(vector))
            .combine((Delegate<Vector<T>>) () -> empty.project().map(vector::append));
    }

    @Override
    public VectorProjection<T> indent(int i) {
        return new VectorProjection<>(vector, empty, separator.indent(i), isEmpty, insert);
    }

    @Override
    public Projection<Vector<T>> delegate() {
        if (vector.isEmpty()) {
            return empty.project()
                .mapChars(chars -> chars.map(c -> c.modify(
                    character -> c.insert(character).map(Vector::of),
                    () -> c.delete().map(Vector::of))))
                .mapChar(c -> c.withStyle("comment.ignored"));
        } else {
            return vector
                .zipWithIndex((e, i) -> e.project()
                    .mapChars(chars -> chars.map(c -> c.modify(
                        character -> c.insert(character).map(t -> vector.update(i, t)),
                        () -> c.delete().map(t -> vector.update(i, t)))))
                    .updateLastChar(c -> c.modify(
                        character -> c.insert(character).orElse(() -> insert.test(character)
                            ? Option.some(vector.insert(i + 1, empty))
                            : Option.none()),
                        () -> c.delete().orElse(() -> i + 1 < vector.size() && isEmpty.test(vector.get(i + 1))
                            ? Option.some(vector.removeAt(i + 1))
                            : Option.none())))
                    .updateFirstChar(c -> c.modify(
                        character -> c.insert(character).orElse(() -> insert.test(character)
                            ? Option.some(vector.insert(i, empty))
                            : Option.none()),
                        () -> c.delete().orElse(() -> i < vector.size() && isEmpty.test(vector.get(i))
                            ? Option.some(vector.removeAt(i))
                            : Option.none()))))
                .intersperse(separator.of(vector))
                .reduce(Projection::combine)
                .map(x -> x.size() == 1 && isEmpty.test(x.head()) ? Vector.empty() : x);
        }
    }
}
