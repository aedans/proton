package io.github.proton.editor;

import io.vavr.control.Option;

public record OptionProjection<T>(Option<T>option,
                                  Projector<T>projector,
                                  T elem) implements Projection.Delegate<Option<T>> {
    @Override
    public Projection<Option<T>> delegate() {
        return option
            .map(x -> projector.project(x)
                .mapChars(c -> new Char<Option<T>>() {
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
                    public Option<Option<T>> insert(char character) {
                        return c.insert(character).map(t -> option.map(x -> t));
                    }

                    @Override
                    public Option<Option<T>> delete() {
                        return c.delete().map(t -> {
                            if (t.equals(elem)) {
                                return Option.none();
                            } else {
                                return option.map(x -> t);
                            }
                        });
                    }
                }))
            .getOrElse(() -> projector.project(elem)
                .mapChars(c -> new Char<Option<T>>() {
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
                    public Option<Option<T>> insert(char character) {
                        return c.insert(character).map(Option::some);
                    }

                    @Override
                    public Option<Option<T>> delete() {
                        return Option.some(option);
                    }
                }));
    }
}
