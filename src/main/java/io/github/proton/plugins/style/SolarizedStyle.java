package io.github.proton.plugins.style;

import io.github.proton.editor.*;
import org.pf4j.Extension;

import java.awt.*;

@Extension
public final class SolarizedStyle implements Style {
    Color base03 = new Color(0x002b36);
    Color base01 = new Color(0x586e75);
    Color base0 = new Color(0x839496);
    Color green = new Color(0x859900);
    Color cyan = new Color(0x2aa198);

    @Override
    public Color background() {
        return base03;
    }

    @Override
    public StyledCharacter base(char character) {
        return new StyledCharacter(character, base0);
    }

    @Override
    public StyledCharacter style(String scope, char character) {
        if (scope.startsWith("keyword")) {
            return base(character).withForegroundColor(green);
        }
        if (scope.startsWith("comment")) {
            return base(character).withForegroundColor(base01);
        }
        if (scope.startsWith("constant.numeric")) {
            return base(character).withForegroundColor(cyan);
        }
        return base(character);
    }
}
