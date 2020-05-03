/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.display;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import io.vavr.collection.Map;
import io.vavr.control.Option;

import java.util.function.Function;

public class Projection<T> {
    public final Map<TerminalPosition, Char<T>> characters;

    public Projection(Map<TerminalPosition, Char<T>> characters) {
        this.characters = characters;
    }

    public int rows() {
        return characters.mapKeys(x -> x.getRow() + 1).keySet().max().getOrElse(0);
    }

    public int columns() {
        return characters.mapKeys(x -> x.getColumn() + 1).keySet().max().getOrElse(0);
    }

    public Projection<T> combineVertical(Projection<T> projection) {
        int rows = rows();
        return new Projection<>(characters.merge(projection.characters.mapKeys(p -> p.withRelativeRow(rows))));
    }

    public Projection<T> combineHorizontal(Projection<T> projection) {
        int cols = columns();
        return new Projection<>(characters.merge(projection.characters.mapKeys(p -> p.withRelativeColumn(cols))));
    }

    public <A> Projection<A> map(Function<T, A> function) {
        return new Projection<>(characters.mapValues(c -> c.map(function)));
    }

    public <A> Projection<A> mapChars(Function<Char<T>, Char<A>> function) {
        return new Projection<>(characters.mapValues(function));
    }

    public <A> Projection<A> of(A a) {
        return map(x -> a);
    }

    public Projection<T> indent(int delta) {
        return new Projection<>(characters.mapKeys(p -> p.withRelativeColumn(delta)));
    }

    public Projection<T> indentVertical(int delta) {
        return new Projection<>(characters.mapKeys(p -> p.withRelativeRow(delta)));
    }

    public interface Char<T> {
        boolean decorative();

        TextCharacter character(Style style);

        Option<T> insert(char character);

        Option<T> delete();

        Option<T> submit();

        default <A> Char<A> map(Function<T, A> map) {
            return new Char<A>() {
                @Override
                public boolean decorative() {
                    return Char.this.decorative();
                }

                @Override
                public TextCharacter character(Style style) {
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

                @Override
                public Option<A> submit() {
                    return Char.this.submit().map(map);
                }
            };
        }
    }
}
