package io.github.proton.display;

import java.awt.*;

public final class StyledCharacter {
    public final char character;
    public final Color foregroundColor;

    public StyledCharacter(char character, Color foregroundColor) {
        this.character = character;
        this.foregroundColor = foregroundColor;
    }

    public StyledCharacter withForegroundColor(Color foregroundColor) {
        return new StyledCharacter(character, foregroundColor);
    }
}
