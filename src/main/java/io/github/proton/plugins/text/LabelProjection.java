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

public final class LabelProjection extends Projection<Line> {
    public static final LabelProjection openBracket = new LabelProjection("{", "punctuation.bracket");
    public static final LabelProjection closeBracket = new LabelProjection("}", "punctuation.bracket");

    public LabelProjection(String string, String scope) {
        this(new Line(string), scope);
    }

    public LabelProjection(Line line, String scope) {
        super(characters(line, scope));
    }

    public static Map<Position, Char<Line>> characters(Line line, String scope) {
        return line.chars.map(c -> new LabelChar(scope, c)).zipWithIndex().toMap(p ->
                new Tuple2<>(new Position(p._2, 0), p._1));
    }

    public static final class LabelChar implements Char<Line> {
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
        public Option<Line> insert(char character) {
            return Option.none();
        }

        @Override
        public Option<Line> delete() {
            return Option.none();
        }

        @Override
        public Option<Line> submit() {
            return Option.none();
        }
    }
}
