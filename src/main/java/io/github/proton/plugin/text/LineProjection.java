package io.github.proton.plugin.text;

import io.github.proton.display.Component;
import io.github.proton.display.Projection;
import io.github.proton.plugin.character.CharacterComponent;
import io.github.proton.plugin.character.InlineCharacterComponent;
import io.vavr.collection.Vector;
import org.pf4j.Extension;

@Extension
public final class LineProjection implements Projection.Of<Line> {
    @Override
    public Class<Line> clazz() {
        return Line.class;
    }

    @Override
    public Vector<Component> project(Line line) {
        Vector<CharacterComponent> characters = line.characters.map(InlineCharacterComponent::new);
        return Vector.of(
                InlineLineComponent.of(characters),
                LiteralLineComponent.of(characters)
        );
    }
}
