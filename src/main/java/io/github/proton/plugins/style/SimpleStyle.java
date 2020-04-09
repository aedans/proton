package io.github.proton.plugins.style;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import io.github.proton.display.Style;
import org.pf4j.Extension;

@Extension
public final class SimpleStyle implements Style {
    @Override
    public TextCharacter base(char character) {
        return new TextCharacter(character, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
    }

    @Override
    public TextCharacter style(String scope, char character) {
        return base(character);
    }
}
