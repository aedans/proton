/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.text;

import io.github.proton.display.Projection;
import io.github.proton.display.Projector;
import org.pf4j.Extension;

@Extension
public final class LineProjector implements Projector<Line> {
    @Override
    public Class<Line> clazz() {
        return Line.class;
    }

    @Override
    public Projection<Line> project(Line line) {
        return new LineProjection(line, "");
    }
}
