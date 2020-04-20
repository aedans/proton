package io.github.proton.plugins.text;

import io.vavr.collection.Vector;

public final class Text {
  public final Vector<Line> lines;

  public Text(Vector<Line> lines) {
    this.lines = lines;
  }
}
