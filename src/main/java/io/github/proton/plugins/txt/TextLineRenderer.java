package io.github.proton.plugins.txt;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import io.github.proton.display.Renderer;
import io.github.proton.display.Screen;

public final class TextLineRenderer implements Renderer<TextLine> {
    @Override
    public Screen render(TextLine textLine, boolean selected) {
        return Screen.from(textLine.chars.observable().map(x -> new TextCharacter(x, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK)));
    }
}
