package io.github.proton.editor;

import io.vavr.control.Option;

import java.awt.*;
import java.util.function.*;

public interface Char<T> {
    boolean decorative();

    boolean mergeable();

    StyledCharacter character(Style style);

    Option<T> insert(char character);

    Option<T> delete();

    static <T> Char<T> empty(char c) {
        return new Char<T>() {
            @Override
            public boolean decorative() {
                return true;
            }

            @Override
            public boolean mergeable() {
                return false;
            }

            @Override
            public StyledCharacter character(Style style) {
                return style.base(c);
            }

            @Override
            public Option<T> insert(char character) {
                return Option.none();
            }

            @Override
            public Option<T> delete() {
                return Option.none();
            }
        };
    }

    default char character() {
        return character(new Style() {
            @Override
            public Color background() {
                return null;
            }

            @Override
            public StyledCharacter base(char character) {
                return new StyledCharacter(character, null);
            }

            @Override
            public StyledCharacter style(String scope, char character) {
                return new StyledCharacter(character, null);
            }
        }).character();
    }

    default Char<T> withDecorative(boolean decorative) {
        return new Delegate<T>() {
            @Override
            public Char<T> delegate() {
                return Char.this;
            }

            @Override
            public boolean decorative() {
                return decorative;
            }
        };
    }

    default Char<T> withMergeable(boolean mergeable) {
        return new Delegate<T>() {
            @Override
            public Char<T> delegate() {
                return Char.this;
            }

            @Override
            public boolean mergeable() {
                return mergeable;
            }
        };
    }

    default Char<T> withCharacter(Function<Style, StyledCharacter> character) {
        return new Delegate<T>() {
            @Override
            public Char<T> delegate() {
                return Char.this;
            }

            @Override
            public StyledCharacter character(Style style) {
                return character.apply(style);
            }
        };
    }

    default Char<T> withInsert(Function<Character, Option<T>> insert) {
        return new Delegate<T>() {
            @Override
            public Char<T> delegate() {
                return Char.this;
            }

            @Override
            public Option<T> insert(char character) {
                return insert.apply(character);
            }
        };
    }

    default Char<T> withDelete(Supplier<Option<T>> delete) {
        return new Delegate<T>() {
            @Override
            public Char<T> delegate() {
                return Char.this;
            }

            @Override
            public Option<T> delete() {
                return delete.get();
            }
        };
    }

    default Char<T> mapStyle(Function<Style, Style> function) {
        return withCharacter(style -> character(function.apply(style)));
    }

    default <A> Char<A> map(Function<T, A> map) {
        return modify(
            character -> insert(character).map(map),
            () -> delete().map(map));
    }

    default <A> Char<A> modify(Function<Character, Option<A>> insert,
                               Supplier<Option<A>> delete) {
        return new Char<A>() {
            @Override
            public boolean decorative() {
                return Char.this.decorative();
            }

            @Override
            public boolean mergeable() {
                return Char.this.mergeable();
            }

            @Override
            public StyledCharacter character(Style style) {
                return Char.this.character(style);
            }

            @Override
            public Option<A> insert(char character) {
                return insert.apply(character);
            }

            @Override
            public Option<A> delete() {
                return delete.get();
            }
        };
    }

    interface Delegate<T> extends Char<T> {
        Char<T> delegate();

        @Override
        default boolean decorative() {
            return delegate().decorative();
        }

        @Override
        default boolean mergeable() {
            return delegate().mergeable();
        }

        @Override
        default StyledCharacter character(Style style) {
            return delegate().character(style);
        }

        @Override
        default Option<T> insert(char character) {
            return delegate().insert(character);
        }

        @Override
        default Option<T> delete() {
            return delegate().delete();
        }
    }
}
