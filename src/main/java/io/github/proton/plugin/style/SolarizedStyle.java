package io.github.proton.plugin.style;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import io.github.proton.display.Style;
import org.pf4j.Extension;

@Extension
public final class SolarizedStyle implements Style {
    TextColor base03 = TextColor.Factory.fromString("#002b36");
    TextColor base0 = TextColor.Factory.fromString("#839496");
    TextColor cyan = TextColor.Factory.fromString("#2aa198");

    @Override
    public TextCharacter base(char character) {
        return new TextCharacter(character, base0, base03);
    }

    @Override
    public TextCharacter style(String scope, char character) {
        if (scope.startsWith("string") || scope.startsWith("constant.character")) {
            return new TextCharacter(character, cyan, base03);
        } else {
            return base(character);
        }
    }
}
