/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.editor;

import io.vavr.collection.Map;
import io.vavr.control.Option;
import java.util.function.Function;

public interface Projection<T> {
    Map<Position, Char<T>> characters();

    static <T> Projection<T> of(Map<Position, Char<T>> characters) {
        return () -> characters;
    }

    default int rows() {
        return characters().mapKeys(x -> x.getRow() + 1).keySet().max().getOrElse(0);
    }

    default int columns() {
        return characters().mapKeys(x -> x.getColumn() + 1).keySet().max().getOrElse(0);
    }

    default Projection<T> combine(Projection<T> projection) {
        int cols = columns();
        return of(characters().merge(projection.characters().mapKeys(p -> p.withRelativeColumn(cols))));
    }

    default Projection<T> combineVertical(Projection<T> projection) {
        int rows = rows();
        return of(characters().merge(projection.characters().mapKeys(p -> p.withRelativeRow(rows))));
    }

    default <A> Projection<A> map(Function<T, A> function) {
        return mapChars(c -> c.map(function));
    }

    default <A> Projection<A> mapChars(Function<Char<T>, Char<A>> function) {
        return of(characters().mapValues(function));
    }

    default <A> Projection<A> of(A a) {
        return map(x -> a);
    }

    default Projection<T> indent(int delta) {
        return of(characters().mapKeys(p -> p.withRelativeColumn(delta)));
    }

    default Projection<T> indentVertical(int delta) {
        return of(characters().mapKeys(p -> p.withRelativeRow(delta)));
    }

    interface Char<T> {
        boolean decorative();

        StyledCharacter character(Style style);

        Option<T> insert(char character);

        Option<T> delete();

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
}
