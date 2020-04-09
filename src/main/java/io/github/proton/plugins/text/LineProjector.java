package io.github.proton.plugins.text;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import io.github.proton.display.Projection;
import io.github.proton.display.Projector;
import io.github.proton.display.Style;
import io.vavr.Tuple2;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;
import org.pf4j.Extension;

@Extension
public final class LineProjector implements Projector<Line> {
    @Override
    public Class<Line> clazz() {
        return Line.class;
    }

    @Override
    public Projection<Line> project(Line line) {
        return new LineProjection(line);
    }

    private static final class LineChar implements Projection.Char<Line> {
        private final Line line;
        private final int i;

        public LineChar(Line line, int i) {
            this.line = line;
            this.i = i;
        }

        @Override
        public TextCharacter character(Style style) {
            return style.base(line.chars.get(i));
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

    private static final class LineProjection implements Projection<Line> {
        private final Line line;

        public LineProjection(Line line) {
            this.line = line;
        }

        @Override
        public Map<TerminalPosition, Char<Line>> characters() {
            return line.chars.<Char<Line>>zipWithIndex((c, i) -> new LineChar(line, i))
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
    }
}
