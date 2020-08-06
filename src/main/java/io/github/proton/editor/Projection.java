package io.github.proton.editor;

import io.vavr.collection.Vector;
import io.vavr.control.Option;

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

    @SafeVarargs
    static <T> Projection<T> chars(Char<T>... chars) {
        return chars(Vector.of(chars));
    }

    static <T> Projection<T> chars(Vector<Char<T>> chars) {
        int length = chars.filter(x -> !x.merge()).length();
        return (width, fit, space, position, indent) -> new Result<>(space - length, position + length, chars);
    }

    static <T> Projection<T> trailing() {
        return chars(Char.trailing());
    }

    static <T> Projection<T> trailingNewline() {
        return Projection.<T>newline().combine(trailing());
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
        } else if (a.last().merge() && !a.last().edit()) {
            return concat(a.init().append(b.head()), b.tail());
        } else if (a.last().merge() && !b.head().edit()) {
            return concat(a.init().append(a.last()
                .withCharacter(b.head().character())
                .withStyle(b.head().style())
                .withEdit(a.last().edit())
                .withMerge(false)), b.tail());
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

    default Vector<Char<T>> chars(int width) {
        return project(width, true, width, 0, 0).chars;
    }

    default <A> Projection<A> map(Function<T, A> f) {
        return (width, fit, space, position, indent) -> project(width, fit, space, position, indent).map(f);
    }

    default <A> Projection<A> mapChar(Function<Char<T>, Char<A>> f) {
        return (width, fit, space, position, indent) -> project(width, fit, space, position, indent).mapChar(f);
    }

    default <A> Projection<A> mapChars(Function<Vector<Char<T>>, Vector<Char<A>>> f) {
        return (width, fit, space, position, indent) -> project(width, fit, space, position, indent).mapChars(f);
    }

    default Projection<T> updateFirstChar(Function<Char<T>, Char<T>> f) {
        return mapChars(chars -> chars.update(chars.zipWithIndex().find(x -> x._1.edit()).get()._2, f));
    }

    default Projection<T> updateLastChar(Function<Char<T>, Char<T>> f) {
        return mapChars(chars -> chars.update(chars.zipWithIndex().findLast(x -> x._1.edit()).get()._2, f));
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

    record Result<T>(int space, int position, Vector<Char<T>> chars) {
        public <A> Result<A> map(Function<T, A> f) {
            return new Result<>(space, position, chars.map(c -> c.map(f)));
        }

        public <A> Result<A> mapChar(Function<Char<T>, Char<A>> f) {
            return mapChars(chars -> chars.map(f));
        }

        public <A> Result<A> mapChars(Function<Vector<Char<T>>, Vector<Char<A>>> f) {
            return new Result<>(space, position, f.apply(chars));
        }
    }
}
