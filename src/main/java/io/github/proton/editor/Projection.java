package io.github.proton.editor;

import io.vavr.collection.*;

import java.util.function.Function;

public interface Projection<T> {
    static <T> Projection<T> empty() {
        return (width, fit, space, position, indent) -> new Result<>(space, position, Vector.empty());
    }

    static <T> Projection<T> newline() {
        return (width, fit, space, position, indent) -> {
            var newline = Char.<T>empty('\n');
            Vector<Char<T>> indents = Vector.fill(indent, () -> Char.empty(' '));
            return new Result<>(width - indent, position + 1, indents.prepend(newline));
        };
    }

    static <T> Projection<T> linebreak() {
        return (width, fit, space, position, indent) -> fit
            ? Projection.<T>empty().project(width, true, space, position, indent)
            : Projection.<T>newline().project(width, false, space, position, indent);
    }

    Result<T> project(int width, boolean fit, int space, int position, int indent);

    default Projection<T> combine(Projection<T> projection) {
        return (width, fit, space, position, indent) -> {
            var a = project(width, fit, space, position, indent);
            var b = projection.project(width, fit, a.space, a.position, indent);
            return new Result<>(b.space, b.position, concat(a.chars, b.chars));
        };
    }

    default Vector<Char<T>> concat(Vector<Char<T>> a, Vector<Char<T>> b) {
        if (a.isEmpty()) {
            return b;
        } else if (b.isEmpty()) {
            return a;
        } else if (a.last().mergeable() && b.head().decorative()) {
            return concat(a.init().append(a.last()
                .withCharacter(b.head()::character)
                .withMergeable(false)), b.tail());
        } else if (a.last().mergeable() && a.last().decorative()) {
            return concat(a.init().append(b.head()), b.tail());
        } else {
            return a.appendAll(b);
        }
    }

    default Projection<T> group() {
        return (width, fit, space, position, indent) -> {
            var endPoint = position + space;
            var result = project(width, true, space, position, indent);
            return result.position <= endPoint ? result : project(width, false, space, position, indent);
        };
    }

    default Projection<T> indent(int i) {
        return (width, fit, space, position, indent) -> project(width, fit, space, position, indent + i);
    }

    default Map<Position, Char<T>> characters() {
        return characters(20);
    }

    default Map<Position, Char<T>> characters(int width) {
        var chars = project(width, true, width, 0, 0).chars;
        var characters = HashMap.<Position, Char<T>>empty();
        int row = 0, col = 0;
        for (Char<T> c : chars) {
            characters = characters.put(new Position(row, col), c);
            if (c.character() == '\n') {
                col = 0;
                row++;
            } else {
                col++;
            }
        }
        return characters;
    }

    default int rows() {
        return characters().mapKeys(x -> x.row() + 1).keySet().max().getOrElse(0);
    }

    default int columns() {
        return characters().mapKeys(x -> x.col() + 1).keySet().max().getOrElse(0);
    }

    default <A> Projection<A> map(Function<T, A> f) {
        return (width, fit, space, position, indent) -> project(width, fit, space, position, indent).map(f);
    }

    default <A> Projection<A> mapChars(Function<Char<T>, Char<A>> f) {
        return (width, fit, space, position, indent) -> project(width, fit, space, position, indent).mapChars(f);
    }

    default <A> Projection<A> of(A a) {
        return map(x -> a);
    }

    interface Delegate<T> extends Projection<T> {
        @Override
        default Result<T> project(int width, boolean fit, int space, int position, int indent) {
            return delegate().project(width, fit, space, position, indent);
        }

        Projection<T> delegate();
    }

    record Result<T>(int space, int position, Vector<Char<T>>chars) {
        public <A> Result<A> map(Function<T, A> f) {
            return new Result<>(space, position, chars.map(c -> c.map(f)));
        }

        public <A> Result<A> mapChars(Function<Char<T>, Char<A>> f) {
            return new Result<>(space, position, chars.map(f));
        }
    }
}
