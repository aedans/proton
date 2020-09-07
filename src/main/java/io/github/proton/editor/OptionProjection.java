package io.github.proton.editor;

import io.vavr.control.Option;

public record OptionProjection<T extends Tree<T>>(Option<T> option,
                                                  T empty) implements Projection.Delegate<Option<T>> {
    @Override
    public Projection<Option<T>> delegate() {
        return option
            .map(x -> x.project().map(t -> t.isEmpty() ? Option.<T>none() : Option.some(t)))
            .getOrElse(() -> empty.project().map(t -> t.isEmpty() ? Option.<T>none() : Option.some(t))
                .mapChar(c -> c.withStyle("comment.ignored")));
    }
}
