package io.github.proton.plugin.character;

import io.github.proton.display.Component;
import io.github.proton.display.Projection;
import io.vavr.collection.Vector;
import org.pf4j.Extension;

@Extension
public final class CharacterProjection implements Projection.Of<Character> {
    @Override
    public Class<Character> clazz() {
        return Character.class;
    }

    @Override
    public Vector<Component> project(Character character) {
        return Vector.of(
                new LiteralCharacterComponent(character),
                new InlineCharacterComponent(character)
        );
    }
}
