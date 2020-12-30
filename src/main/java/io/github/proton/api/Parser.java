package io.github.proton.api;

import org.reactfx.util.*;

import java.util.*;
import java.util.function.*;

public interface Parser<V> {
    Either<V, Error> parse(Parse parse);

    String name();

    final class Error {
        private final Supplier<String> name;

        public Error(Supplier<String> name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name.get();
        }
    }

    final class Parse {
        public final CharSequence input;
        public int pos = 0;

        public Parse(CharSequence input) {
            this.input = input;
        }

        public char consume(char c) {
            return consume(x -> x == c);
        }

        public char consume(Predicate<Character> p) {
            if (input.length() > pos && p.test(input.charAt(pos))) {
                pos++;
                return input.charAt(pos - 1);
            }
            return 0;
        }
    }

    default Either<V, Error> parse(CharSequence input) {
        return parse(new Parse(input));
    }

    Parser<?> EOF = new Parser<>() {
        @Override
        public Either<Object, Error> parse(Parse parser) {
            return parser.pos == parser.input.length() ? Either.left(null) : Either.right(new Error(this::name));
        }

        @Override
        public String name() {
            return "EOF";
        }
    };

    class Delegate<V> implements Parser<V> {
        private final Parser<V> parser;

        public Delegate(Parser<V> parser) {
            this.parser = parser;
        }

        @Override
        public Either<V, Error> parse(Parse parse) {
            return parser.parse(parse);
        }

        @Override
        public String name() {
            return parser.name();
        }
    }

    static Parser<Character> cond(String name, Predicate<Character> p) {
        return new Parser<>() {
            @Override
            public Either<Character, Error> parse(Parse parse) {
                char c;
                if ((c = parse.consume(p)) == 0) {
                    return Either.right(new Error(() -> name));
                }
                return Either.left(c);
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    static Parser<String> of(String string) {
        return of(string, string);
    }

    static <V> Parser<V> of(String string, V v) {
        return new Parser<>() {
            @Override
            public Either<V, Error> parse(Parse parse) {
                for (int i = 0; i < string.length(); i++) {
                    if (parse.consume(string.charAt(i)) == 0) {
                        return Either.right(new Error(this::name));
                    }
                }
                return Either.left(v);
            }

            @Override
            public String name() {
                return "'" + string + "'";
            }
        };
    }

    static <V> Parser<V> ref(String name, Supplier<Parser<V>> s) {
        return new Parser<>() {
            @Override
            public Either<V, Error> parse(Parse parse) {
                return s.get().parse(parse);
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    @SafeVarargs
    static <V> Parser<V> or(Parser<V>... parsers) {
        return new Parser<>() {
            @Override
            public Either<V, Error> parse(Parse parse) {
                int origPos = parse.pos;
                for (Parser<V> parser : parsers) {
                    parse.pos = origPos;
                    Either<V, Error> cst = parser.parse(parse);
                    if (cst.isLeft()) {
                        return cst;
                    }
                }
                return Either.right(new Error(this::name));
            }

            @Override
            public String name() {
                String[] names = Arrays.stream(parsers).map(Parser::name).toArray(String[]::new);
                return String.join(" | ", names);
            }
        };
    }

    default Parser<Positioned<V>> positioned() {
        return new Parser<>() {
            @Override
            public Either<Positioned<V>, Error> parse(Parse parse) {
                int start = parse.pos;
                Either<V, Error> eval = Parser.this.parse(parse);
                int end = parse.pos;
                return eval.mapLeft(t -> new Positioned<>(t, start, end));
            }

            @Override
            public String name() {
                return Parser.this.name();
            }
        };
    }

    default <V2> Parser<V2> map(Function<V, V2> f) {
        return new Parser<>() {
            @Override
            public Either<V2, Error> parse(Parse parse) {
                return Parser.this.parse(parse).mapLeft(f);
            }

            @Override
            public String name() {
                return Parser.this.name();
            }
        };
    }

    default <V2, V3> Parser<V3> then(Parser<V2> parser, BiFunction<V, V2, V3> f) {
        return new Parser<>() {
            @Override
            public Either<V3, Error> parse(Parse parse) {
                Either<V, Error> fst = Parser.this.parse(parse);
                if (fst.isRight()) {
                    return Either.right(fst.getRight());
                }
                Either<V2, Error> snd = parser.parse(parse);
                if (snd.isRight()) {
                    return Either.right(snd.getRight());
                }
                return Either.left(f.apply(fst.getLeft(), snd.getLeft()));
            }

            @Override
            public String name() {
                return Parser.this.name() + ", " + parser.name();
            }
        };
    }

    default <V2, V3, V4> Parser<V4> then2(Parser<V2> a, Parser<V3> b, TriFunction<V, V2, V3, V4> f) {
        return new Parser<>() {
            @Override
            public Either<V4, Error> parse(Parse parse) {
                Either<V, Error> fst = Parser.this.parse(parse);
                if (fst.isRight()) {
                    return Either.right(fst.getRight());
                }
                Either<V2, Error> snd = a.parse(parse);
                if (snd.isRight()) {
                    return Either.right(snd.getRight());
                }
                Either<V3, Error> trd = b.parse(parse);
                if (trd.isRight()) {
                    return Either.right(trd.getRight());
                }
                return Either.left(f.apply(fst.getLeft(), snd.getLeft(), trd.getLeft()));
            }

            @Override
            public String name() {
                return Parser.this.name() + ", " + a.name() + ", " + b.name();
            }
        };
    }

    default Parser<V> thenL(Parser<?> parser) {
        return then(parser, (a, b) -> a);
    }

    default <V2> Parser<V2> thenR(Parser<V2> parser) {
        return then(parser, (a, b) -> b);
    }

    default Parser<Optional<V>> option() {
        return new Parser<>() {
            @Override
            public Either<Optional<V>, Error> parse(Parse parse) {
                Either<V, Error> cst = Parser.this.parse(parse);
                if (cst.isLeft()) {
                    return Either.left(Optional.of(cst.getLeft()));
                } else {
                    return Either.left(Optional.empty());
                }
            }

            @Override
            public String name() {
                return Parser.this.name() + "?";
            }
        };
    }

    default Parser<V> when(Predicate<V> predicate) {
        return new Parser<V>() {
            @Override
            public Either<V, Error> parse(Parse parse) {
                int origPos = parse.pos;
                Either<V, Error> cst = Parser.this.parse(parse);
                if (cst.isLeft() && predicate.test(cst.getLeft())) {
                    return cst;
                } else {
                    parse.pos = origPos;
                    return Either.right(new Error(Parser.this::name));
                }
            }

            @Override
            public String name() {
                return Parser.this.name();
            }
        };
    }

    default Parser<List<V>> many() {
        return new Parser<>() {
            @Override
            public Either<List<V>, Error> parse(Parse parse) {
                List<V> ans = new ArrayList<>();
                while (true) {
                    int origPos = parse.pos;
                    Either<V, Error> cst = Parser.this.parse(parse);
                    if (cst.isLeft()) {
                        ans.add(cst.getLeft());
                    } else {
                        parse.pos = origPos;
                        break;
                    }
                }
                return Either.left(ans);
            }

            @Override
            public String name() {
                return Parser.this.name() + "*";
            }
        };
    }

    default Parser<List<V>> many1() {
        return then(many(), (first, rest) -> {
            rest.add(0, first);
            return rest;
        });
    }

    default Parser<List<V>> sepBy(Parser<?> sep) {
        return then(sep.thenR(many1()), (first, rest) -> {
            rest.add(0, first);
            return rest;
        });
    }
}
