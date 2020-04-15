package io.github.proton.plugins.text;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import io.github.proton.display.Projection;
import io.github.proton.display.Style;
import io.vavr.Tuple2;
import io.vavr.collection.Map;

final class LineProjection implements Projection<Line> {
    private final Line line;
    private final String scope;

    public LineProjection(Line line, String scope) {
        this.line = line;
        this.scope = scope;
    }

    @Override
    public Map<TerminalPosition, Char<Line>> characters() {
        return line.chars.<Char<Line>>zipWithIndex((c, i) -> new LineChar(line, scope, i))
                .append(new Char<Line>() {
                    @Override
                    public TextCharacter character(Style style) {
                        return style.base(' ');
                    }

                    @Override
                    public Line insert(char c) {
                        return new Line(line.chars.append(c));
                    }

                    @Override
                    public Line delete() {
                        return line;
                    }

                    @Override
                    public Line submit() {
                        return line;
                    }
                })
                .zipWithIndex()
                .toMap(p -> new Tuple2<>(new TerminalPosition(p._2, 0), p._1));
    }

    private static final class LineChar implements Projection.Char<Line> {
        private final Line line;
        private final String scope;
        private final int i;

        public LineChar(Line line, String scope, int i) {
            this.line = line;
            this.scope = scope;
            this.i = i;
        }

        @Override
        public TextCharacter character(Style style) {
            return style.style(scope, line.chars.get(i));
        }

        @Override
        public Line insert(char c) {
            return new Line(line.chars.insert(i, c));
        }

        @Override
        public Line delete() {
            return new Line(line.chars.removeAt(i));
        }

        @Override
        public Line submit() {
            return line;
        }
    }
}