/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.display;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import java.util.function.Function;

public interface Projection<T> {
    Map<TerminalPosition, Char<T>> characters();

    default int rows() {
        return characters().mapKeys(x -> x.getRow() + 1).keySet().max().getOrElse(0);
    }

    default int columns() {
        return characters().mapKeys(x -> x.getColumn() + 1).keySet().max().getOrElse(0);
    }

    default Projection<T> combineVertical(Projection<T> projection) {
        int rows = rows();
        return () -> characters().merge(projection.characters().mapKeys(p -> p.withRelativeRow(rows)));
    }

    default Projection<T> combineHorizontal(Projection<T> projection) {
        int cols = columns();
        return () -> characters().merge(projection.characters().mapKeys(p -> p.withRelativeColumn(cols)));
    }

    default <A> Projection<A> map(Function<T, A> function) {
        return () -> characters().mapValues(c -> c.map(function));
    }

    default <A> Projection<A> of(A a) {
        return map(x -> a);
    }

    default Projection<T> indent(int delta) {
        return () -> characters().mapKeys(p -> p.withRelativeColumn(delta));
    }

    default Projection<T> indentVertical(int delta) {
        return () -> characters().mapKeys(p -> p.withRelativeRow(delta));
    }

    interface Char<T> {
        boolean decorative();

        TextCharacter character(Style style);

        Option<T> insert(char c);

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
                public Option<A> insert(char c) {
                    return Char.this.insert(c).map(map);
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
