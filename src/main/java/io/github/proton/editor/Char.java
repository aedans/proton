package io.github.proton.editor;

import io.vavr.control.Option;

import java.util.function.*;

public interface Char<T> {
    boolean edit();

    boolean merge();

    char character();

    String style();

    Option<T> insert(char character);

    Option<T> delete();

    static <T> Char<T> trailing() {
        return Char.<T>empty(' ').withEdit(true).withMerge(true);
    }

    static <T> Char<T> empty(char c) {
        return new Char<T>() {
            @Override
            public boolean edit() {
                return false;
            }

            @Override
            public boolean merge() {
                return false;
            }

            @Override
            public char character() {
                return c;
            }

            @Override
            public String style() {
                return "";
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

    default Char<T> withEdit(boolean edit) {
        return new Delegate<T>() {
            @Override
            public Char<T> delegate() {
                return Char.this;
            }

            @Override
            public boolean edit() {
                return edit;
            }
        };
    }

    default Char<T> withMerge(boolean merge) {
        return new Delegate<T>() {
            @Override
            public Char<T> delegate() {
                return Char.this;
            }

            @Override
            public boolean merge() {
                return merge;
            }
        };
    }

    default Char<T> withCharacter(char character) {
        return new Delegate<T>() {
            @Override
            public Char<T> delegate() {
                return Char.this;
            }

            @Override
            public char character() {
                return character;
            }
        };
    }

    default Char<T> withStyle(String style) {
        return new Delegate<T>() {
            @Override
            public Char<T> delegate() {
                return Char.this;
            }

            @Override
            public String style() {
                return style;
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

    default <A> Char<A> map(Function<T, A> map) {
        return modify(
            character -> insert(character).map(map),
            () -> delete().map(map));
    }

    default <A> Char<A> modify(Function<Character, Option<A>> insert,
                               Supplier<Option<A>> delete) {
        return new Char<A>() {
            @Override
            public boolean edit() {
                return Char.this.edit();
            }

            @Override
            public boolean merge() {
                return Char.this.merge();
            }

            @Override
            public char character() {
                return Char.this.character();
            }

            @Override
            public String style() {
                return Char.this.style();
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
        default boolean edit() {
            return delegate().edit();
        }

        @Override
        default boolean merge() {
            return delegate().merge();
        }

        @Override
        default char character() {
            return delegate().character();
        }

        @Override
        default String style() {
            return delegate().style();
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
