package io.github.proton.plugin.character;

import io.github.proton.display.Component;
import io.github.proton.display.Projection;
import io.vavr.collection.Vector;
import org.pf4j.Extension;

@Extension
public final class CharacterProjection extends Projection.Of<Character> {
    public CharacterProjection() {
        super(Character.class);
    }

    @Override
    public Vector<Component> project(Character character) {
        return Vector.of(
                new InlineCharacterComponent(character),
                new LiteralCharacterComponent(character)
        );
    }
}
