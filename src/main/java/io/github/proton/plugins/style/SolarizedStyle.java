package io.github.proton.plugins.style;

import static com.googlecode.lanterna.TextColor.Factory.fromString;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import io.github.proton.display.Style;
import org.pf4j.Extension;

@Extension
public final class SolarizedStyle implements Style {
  TextColor base03 = fromString("#002b36");
  TextColor base0 = fromString("#839496");
  TextColor green = fromString("#859900");

  @Override
  public TextCharacter base(char character) {
    return new TextCharacter(character, base0, base03);
  }

  @Override
  public TextCharacter style(String scope, char character) {
    switch (scope) {
      case "keyword":
        return base(character).withForegroundColor(green);
    }
    return base(character);
  }
}
