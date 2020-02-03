package io.github.proton.plugin.text;

import io.github.proton.plugin.character.CharacterComponent;
import io.github.proton.plugin.character.InlineCharacterComponent;
import io.vavr.collection.Vector;

public final class InlineLineComponent implements LineComponent {
    public final Vector<CharacterComponent> components;
    public final int index;

    public InlineLineComponent(Vector<CharacterComponent> components, int index) {
        this.components = components;
        this.index = index;
    }

    public static InlineLineComponent of(Vector<CharacterComponent> characters) {
        characters = characters.append(characters.last().setCharacter(' '));
        return new InlineLineComponent(characters, 0);
    }

    @Override
    public Vector<CharacterComponent> getComponents() {
        return components;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public LineComponent setComponents(Vector<CharacterComponent> components) {
        return new InlineLineComponent(components, index);
    }

    @Override
    public LineComponent setIndex(int index) {
        return new InlineLineComponent(components, index);
    }
}
