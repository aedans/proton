package io.github.proton.plugins.style;

import io.github.proton.editor.*;
import org.pf4j.Extension;

import java.awt.*;

@Extension
public final class SolarizedStyle implements Style {
    Color base03 = new Color(0x00, 0x2b, 0x36);
    Color base01 = new Color(0x58, 0x6e, 0x75);
    Color base0 = new Color(0x83, 0x94, 0x96);
    Color green = new Color(0x85, 0x99, 0x00);
    Color blue = new Color(0x26, 0x8b, 0xd2);

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
        if (scope.startsWith("entity.name.class") || scope.startsWith("entity.name.type.class")) {
            return base(character).withForegroundColor(blue);
        }
        return base(character);
    }
}
