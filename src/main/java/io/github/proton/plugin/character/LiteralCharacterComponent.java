package io.github.proton.plugin.character;

import io.github.proton.display.Screen;
import io.github.proton.display.Style;

public final class LiteralCharacterComponent implements CharacterComponent {
    public final char character;

    public LiteralCharacterComponent(char character) {
        this.character = character;
    }

    @Override
    public char getCharacter() {
        return character;
    }

    @Override
    public CharacterComponent setCharacter(char character) {
        return new LiteralCharacterComponent(character);
    }

    @Override
    public Screen render(Style style, boolean selected) {
        Screen screen = Screen.of(
                style.style("constant.character", '\''),
                style.style("constant.character", character),
                style.style("constant.character", '\'')
        );
        return selected ? screen.invert() : screen;
    }

    @Override
    public String toString() {
        return "LiteralCharacterComponent{" +
                "character=" + character +
                '}';
    }
}
