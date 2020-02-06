package io.github.proton.plugin.character;

import io.github.proton.display.Screen;
import io.github.proton.display.Style;

public final class InlineCharacterComponent implements CharacterComponent {
    public final char character;

    public InlineCharacterComponent(char character) {
        this.character = character;
    }

    @Override
    public char getCharacter() {
        return character;
    }

    @Override
    public CharacterComponent setCharacter(char character) {
        return new InlineCharacterComponent(character);
    }

    @Override
    public Screen render(Style style, boolean selected) {
        Screen screen = Screen.of(style.base(character));
        return selected ? screen.invert() : screen;
    }

    @Override
    public String toString() {
        return "InlineCharacterComponent{" +
                "character=" + character +
                '}';
    }
}
