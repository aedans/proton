/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.editor;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;

public final class TextProjection implements Projection<Text> {
    public static final TextProjection openBracket = label("{", "punctuation.bracket");
    public static final TextProjection closeBracket = label("}", "punctuation.bracket");

    private final Text text;
    private final String scope;
    private final boolean decorative;

    private TextProjection(String line, String scope, boolean decorative) {
        this(new Text(line), scope, decorative);
    }

    private TextProjection(Text text, String scope, boolean decorative) {
        this.text = text;
        this.scope = scope;
        this.decorative = decorative;
    }

    public static TextProjection text(String line, String scope) {
        return new TextProjection(line, scope, false);
    }

    public static TextProjection text(Text text, String scope) {
        return new TextProjection(text, scope, false);
    }

    public static TextProjection label(String line, String scope) {
        return new TextProjection(line, scope, true);
    }

    public static TextProjection label(Text text, String scope) {
        return new TextProjection(text, scope, true);
    }

    @Override
    public Map<Position, Char<Text>> characters() {
        int index = 0, row = 0, col = 0;
        Map<Position, Char<Text>> chars = HashMap.empty();
        for (char c : text.chars) {
            chars = chars.put(new Position(row, col), new TextChar(text, scope, decorative, index));
            index++;
            if (c == '\n') {
                col = 0;
                row++;
            } else {
                col++;
            }
        }
        return chars.put(new Position(row, col), new Char<Text>() {
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
                return Option.some(new Text(text.chars.append(character)));
            }

            @Override
            public Option<Text> delete() {
                return Option.none();
            }
        });
    }

    private static final class TextChar implements Projection.Char<Text> {
        private final Text text;
        private final String scope;
        private final boolean decorative;
        private final int i;

        public TextChar(Text text, String scope, boolean decorative, int i) {
            this.text = text;
            this.scope = scope;
            this.decorative = decorative;
            this.i = i;
        }

        @Override
        public boolean decorative() {
            return decorative;
        }

        @Override
        public StyledCharacter character(Style style) {
            return style.style(scope, text.chars.get(i));
        }

        @Override
        public Option<Text> insert(char character) {
            return Option.some(new Text(text.chars.insert(i, character)));
        }

        @Override
        public Option<Text> delete() {
            return Option.some(new Text(text.chars.removeAt(i)));
        }
    }
}
