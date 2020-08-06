package io.github.proton.editor;

import io.vavr.control.Option;

import java.util.function.*;

public record OptionProjection<T>(Option<T> option,
                                  Function<T, Projection<T>> projector,
                                  T empty,
                                  Predicate<T> isEmpty) implements Projection.Delegate<Option<T>> {
    @Override
    public Projection<Option<T>> delegate() {
        return option
            .map(x -> projector.apply(x).map(t -> isEmpty.test(t) ? Option.<T>none() : Option.some(t)))
            .getOrElse(() -> projector.apply(empty).map(t -> isEmpty.test(t) ? Option.<T>none() : Option.some(t))
                .mapChar(c -> c.withStyle("comment.ignored")));
    }
}
