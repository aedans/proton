package io.github.proton.plugin.text;

import io.github.proton.display.Screen;
import io.github.proton.display.Style;
import io.github.proton.plugin.character.CharacterComponent;
import io.github.proton.plugin.character.InlineCharacterComponent;
import io.vavr.collection.Vector;

public final class LiteralLineComponent implements LineComponent {
    public final Vector<CharacterComponent> components;
    public final int index;

    public LiteralLineComponent(Vector<CharacterComponent> components, int index) {
        this.components = components;
        this.index = index;
    }

    public static LiteralLineComponent of(Vector<CharacterComponent> characters) {
        characters = characters.append(new InlineCharacterComponent('"'));
        return new LiteralLineComponent(characters, 0);
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
        return new LiteralLineComponent(components, index);
    }

    @Override
    public LineComponent setIndex(int index) {
        return new LiteralLineComponent(components, index);
    }

    @Override
    public Screen render(Style style, boolean selected) {
        Screen screen = new InlineLineComponent(components, index)
                .render(style.withBase("string"), selected);
        Screen quote = Screen.of(style.style("string", '"'));
        return quote.horizontalPlus(screen);
    }
}
