package io.github.proton.editor;

import io.vavr.control.Option;

import java.util.function.Predicate;

public record OptionProjection<T extends Tree<T>>(Option<T> option,
                                                  T empty,
                                                  Predicate<T> isEmpty) implements Projection.Delegate<Option<T>> {
    @Override
    public Projection<Option<T>> delegate() {
        return option
            .map(x -> x.project().map(t -> isEmpty.test(t) ? Option.<T>none() : Option.some(t)))
            .getOrElse(() -> empty.project().map(t -> isEmpty.test(t) ? Option.<T>none() : Option.some(t))
                .mapChar(c -> c.withStyle("comment.ignored")));
    }
}
