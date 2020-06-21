package io.github.proton.editor;

import io.vavr.collection.*;
import io.vavr.control.Option;

import java.util.function.Function;

public interface Projection<T> {
    Projection<Text> openBracket = label("{", "punctuation.bracket");
    Projection<Text> closeBracket = label("}", "punctuation.bracket");

    static <T> Projection<T> empty() {
        return (width, fit, space, position, indent) -> new Result<>(space, position, Vector.empty());
    }

    static <T> Projection<T> newline() {
        return (width, fit, space, position, indent) -> {
            var newline = new Char<T>() {
                @Override
                public boolean decorative() {
                    return true;
                }

                @Override
                public StyledCharacter character(Style style) {
                    return style.base('\n');
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
            Vector<Char<T>> indents = Vector.range(0, indent).map(x -> new Char<T>() {
                @Override
                public boolean decorative() {
                    return true;
                }

                @Override
                public StyledCharacter character(Style style) {
                    return style.base(' ');
                }

                @Override
                public Option<T> insert(char character) {
                    return Option.none();
                }

                @Override
                public Option<T> delete() {
                    return Option.none();
                }
            });
            return new Result<>(width - indent, position + 1, indents.prepend(newline));
        };
    }

    static <T> Projection<T> linebreak() {
        return (width, fit, space, position, indent) -> fit
                ? Projection.<T>empty().project(width, true, space, position, indent)
                : Projection.<T>newline().project(width, false, space, position, indent);
    }

    static Projection<Text> text(String text, String scope) {
        return text(new Text(text), scope);
    }

    static Projection<Text> text(Text text, String scope) {
        return string(text, scope, false);
    }

    static Projection<Text> label(String text, String scope) {
        return label(new Text(text), scope);
    }

    static Projection<Text> label(Text text, String scope) {
        return string(text, scope, true);
    }

    static Projection<Text> string(Text text, String scope, boolean decorative) {
        return (width, fit, space, position, indent) -> {
            var length = text.chars().length();
            var trail = new Char<Text>() {
                @Override
                public boolean decorative() {
                    return decorative;
                }

                @Override
                public StyledCharacter character(Style style) {
                    return style.base(' ');
                }

                @Override
                public Option<Text> insert(char character) {
                    return Option.some(new Text(text.chars().append(character)));
                }

                @Override
                public Option<Text> delete() {
                    return Option.none();
                }
            };
            Vector<Char<Text>> chars = text.chars().zipWithIndex((c, i) -> new Char<Text>() {
                @Override
                public boolean decorative() {
                    return decorative;
                }

                @Override
                public StyledCharacter character(Style style) {
                    return style.style(scope, text.chars().get(i));
                }

                @Override
                public Option<Text> insert(char character) {
                    return Option.some(new Text(text.chars().insert(i, character)));
                }

                @Override
                public Option<Text> delete() {
                    return Option.some(new Text(text.chars().removeAt(i)));
                }
            });
            return new Result<>(space - length, position + length, chars.append(trail));
        };
    }

    Result<T> project(int width, boolean fit, int space, int position, int indent);

    default Projection<T> combine(Projection<T> projection) {
        return (width, fit, space, position, indent) -> {
            var a = project(width, fit, space, position, indent);
            var b = projection.project(width, fit, a.space, a.position, indent);
            return new Result<>(b.space, b.position, a.chars.appendAll(b.chars));
        };
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

    record Result<T>(int space, int position, Vector<Char<T>>chars) {
        public <A> Result<A> map(Function<T, A> f) {
            return new Result<>(space, position, chars.map(c -> c.map(f)));
        }

        public <A> Result<A> mapChars(Function<Char<T>, Char<A>> f) {
            return new Result<>(space, position, chars.map(f));
        }
    }
}
