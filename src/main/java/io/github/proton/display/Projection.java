package io.github.proton.display;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import io.vavr.collection.Map;

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
        return () -> characters().merge(projection.characters()
                .mapKeys(p -> p.withRelativeRow(rows)));
    }

    default Projection<T> combineHorizontal(Projection<T> projection) {
        int cols = columns();
        return () -> characters().merge(projection.characters()
                .mapKeys(p -> p.withRelativeColumn(cols)));
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

    interface Char<T> {
        TextCharacter character(Style style);

        T insert(char c);

        T delete();

        T submit();

        default Char<T> onSubmit(T tree) {
            return new Char<T>() {
                @Override
                public TextCharacter character(Style style) {
                    return Char.this.character(style);
                }

                @Override
                public T insert(char c) {
                    return Char.this.insert(c);
                }

                @Override
                public T delete() {
                    return Char.this.delete();
                }

                @Override
                public T submit() {
                    return tree;
                }
            };
        }

        default <A> Char<A> map(Function<T, A> map) {
            return new Char<A>() {
                @Override
                public TextCharacter character(Style style) {
                    return Char.this.character(style);
                }

                @Override
                public A insert(char c) {
                    return map.apply(Char.this.insert(c));
                }

                @Override
                public A delete() {
                    return map.apply(Char.this.delete());
                }

                @Override
                public A submit() {
                    return map.apply(Char.this.submit());
                }
            };
        }
    }
}
