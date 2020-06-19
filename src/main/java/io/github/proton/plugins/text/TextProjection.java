/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.text;

import io.github.proton.display.Position;
import io.github.proton.display.Projection;
import io.github.proton.display.Style;
import io.github.proton.display.StyledCharacter;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;

public final class TextProjection extends Projection<Text> {
    public TextProjection(String line, String scope) {
        this(new Text(line), scope);
    }

    public TextProjection(Text text, String scope) {
        super(characters(text, scope));
    }

    public static Map<Position, Char<Text>> characters(Text text, String scope) {
        int index = 0, row = 0, col = 0;
        Map<Position, Char<Text>> chars = HashMap.empty();
        for (char c : text.chars) {
            chars = chars.put(new Position(row, col), new LineChar(text, scope, index));
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
                return false;
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

    private static final class LineChar implements Projection.Char<Text> {
        private final Text text;
        private final String scope;
        private final int i;

        public LineChar(Text text, String scope, int i) {
            this.text = text;
            this.scope = scope;
            this.i = i;
        }

        @Override
        public boolean decorative() {
            return false;
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
