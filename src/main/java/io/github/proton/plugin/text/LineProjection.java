package io.github.proton.plugin.text;

import io.github.proton.Plugins;
import io.github.proton.display.Component;
import io.github.proton.display.Projection;
import io.github.proton.plugin.character.CharacterComponent;
import io.vavr.collection.Vector;
import org.pf4j.Extension;

@Extension
public final class LineProjection extends Projection.Of<Line> {
    public LineProjection() {
        super(Line.class);
    }

    @Override
    public Vector<Component> project(Line line) {
        Vector<CharacterComponent> characters = line.characters.map(x ->
                (CharacterComponent) Plugins.projection().projectGeneric(x).get());
        return Vector.of(
                InlineLineComponent.of(characters),
                LiteralLineComponent.of(characters)
        );
    }
}
