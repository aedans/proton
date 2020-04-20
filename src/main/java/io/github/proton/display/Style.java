package io.github.proton.display;

import com.googlecode.lanterna.TextCharacter;
import org.pf4j.ExtensionPoint;

public interface Style extends ExtensionPoint {
  TextCharacter base(char character);

  TextCharacter style(String scope, char character);

  default Style withBase(String scope) {
    return new Style() {
      @Override
      public TextCharacter base(char character) {
        return Style.this.style(scope, character);
      }

      @Override
      public TextCharacter style(String scope, char character) {
        return Style.this.style(scope, character);
      }
    };
  }
}
