/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.editor;

import io.vavr.control.Option;
import java.awt.*;
import java.util.function.Function;

public interface Char<T> {
    boolean decorative();

    StyledCharacter character(Style style);

    Option<T> insert(char character);

    Option<T> delete();

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
                })
                .character;
    }

    default <A> Char<A> map(Function<T, A> map) {
        return new Char<A>() {
            @Override
            public boolean decorative() {
                return Char.this.decorative();
            }

            @Override
            public StyledCharacter character(Style style) {
                return Char.this.character(style);
            }

            @Override
            public Option<A> insert(char character) {
                return Char.this.insert(character).map(map);
            }

            @Override
            public Option<A> delete() {
                return Char.this.delete().map(map);
            }
        };
    }
}
