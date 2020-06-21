package io.github.proton.editor;

import java.awt.*;

public record StyledCharacter(char character, Color foregroundColor) {
    public StyledCharacter withForegroundColor(Color foregroundColor) {
        return new StyledCharacter(character, foregroundColor);
    }
}
