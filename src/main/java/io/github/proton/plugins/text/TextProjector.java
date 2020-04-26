/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.text;

import io.github.proton.display.Projection;
import io.github.proton.display.Projector;
import io.github.proton.display.VectorProjection;
import org.pf4j.Extension;

@Extension
public final class TextProjector implements Projector<Text> {
    @Override
    public Class<Text> clazz() {
        return Text.class;
    }

    @Override
    public Projection<Text> project(Text text) {
        return new VectorProjection<>(text.lines, Projector.get(Line.class), new Line(""))
                .map(Text::new);
    }
}
