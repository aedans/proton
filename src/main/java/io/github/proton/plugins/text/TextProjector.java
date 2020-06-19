/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.text;

import io.github.proton.editor.Projection;
import io.github.proton.editor.Projector;
import io.github.proton.editor.Text;
import io.github.proton.editor.TextProjection;
import org.pf4j.Extension;

@Extension
public final class TextProjector implements Projector<Text> {
    @Override
    public Class<Text> clazz() {
        return Text.class;
    }

    @Override
    public Projection<Text> project(Text text) {
        return TextProjection.text(text, "");
    }
}
