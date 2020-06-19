/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.text;

import io.github.proton.display.Position;
import io.github.proton.display.Projection;
import io.github.proton.display.Style;
import io.github.proton.display.StyledCharacter;
import io.vavr.Tuple2;
import io.vavr.collection.Map;
import io.vavr.control.Option;

public final class LabelProjection extends Projection<Text> {
    public static final LabelProjection openBracket = new LabelProjection("{", "punctuation.bracket");
    public static final LabelProjection closeBracket = new LabelProjection("}", "punctuation.bracket");

    public LabelProjection(String string, String scope) {
        this(new Text(string), scope);
    }

    public LabelProjection(Text text, String scope) {
        super(characters(text, scope));
    }

    public static Map<Position, Char<Text>> characters(Text text, String scope) {
        return text.chars.map(c -> new LabelChar(scope, c)).zipWithIndex().toMap(p ->
                new Tuple2<>(new Position(0, p._2), p._1));
    }

    public static final class LabelChar implements Char<Text> {
        private final String scope;
        private final char c;

        public LabelChar(String scope, char c) {
            this.scope = scope;
            this.c = c;
        }

        @Override
        public boolean decorative() {
            return true;
        }

        @Override
        public StyledCharacter character(Style style) {
            return style.style(scope, c);
        }

        @Override
        public Option<Text> insert(char character) {
            return Option.none();
        }

        @Override
        public Option<Text> delete() {
            return Option.none();
        }
    }
}
