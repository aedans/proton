package io.github.proton.plugins.text;

import io.github.proton.display.GroupProjection;
import io.github.proton.display.Projection;
import io.github.proton.display.Projector;
import io.vavr.collection.Vector;
import org.pf4j.Extension;

@Extension
public final class TextProjector implements Projector<Text> {
  @Override
  public Class<Text> clazz() {
    return Text.class;
  }

  @Override
  public Projection<Text> project(Text text) {
    Projector<Line> lineProjector = Projector.get(Line.class);
    return new GroupProjection<Text, Line>() {
      @Override
      public Projection<Line> projectElem(Line elem) {
        return lineProjector.project(elem);
      }

      @Override
      public Vector<Line> getElems() {
        return text.lines;
      }

      @Override
      public Text setElems(Vector<Line> lines) {
        return new Text(lines);
      }
    };
  }
}
