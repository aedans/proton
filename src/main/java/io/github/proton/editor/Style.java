/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.editor;

import java.awt.*;
import org.pf4j.ExtensionPoint;

public interface Style extends ExtensionPoint {
    Color background();

    StyledCharacter base(char character);

    StyledCharacter style(String scope, char character);

    default Style of(String scope) {
        return new Style() {
            @Override
            public Color background() {
                return Style.this.background();
            }

            @Override
            public StyledCharacter base(char character) {
                return Style.this.style(scope, character);
            }

            @Override
            public StyledCharacter style(String s, char character) {
                return Style.this.style(scope, character);
            }
        };
    }
}
