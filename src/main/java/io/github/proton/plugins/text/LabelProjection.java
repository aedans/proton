package io.github.proton.plugins.text;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import io.github.proton.display.Projection;
import io.github.proton.display.Style;
import io.vavr.Tuple2;
import io.vavr.collection.Map;
import io.vavr.control.Option;

public final class LabelProjection implements Projection<Line> {
    public static final LabelProjection whitespace = new LabelProjection(" ", "base");
    private final Line line;
    private final String scope;

    public LabelProjection(String string, String scope) {
        this(new Line(string), scope);
    }

    public LabelProjection(Line line, String scope) {
        this.line = line;
        this.scope = scope;
    }

    @Override
    public Map<TerminalPosition, Char<Line>> characters() {
        return line.chars
                .map(c -> new LabelChar(line, scope, c))
                .append(new LabelChar(line, "", ' '))
                .zipWithIndex()
                .toMap(p -> new Tuple2<>(new TerminalPosition(p._2, 0), p._1));
    }

    public static final class LabelChar implements Char<Line> {
        private final Line line;
        private final String scope;
        private final char c;

        public LabelChar(Line line, String scope, char c) {
            this.line = line;
            this.scope = scope;
            this.c = c;
        }

        @Override
        public TextCharacter character(Style style) {
            return style.style(scope, c);
        }

        @Override
        public Option<Line> insert(char c) {
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
