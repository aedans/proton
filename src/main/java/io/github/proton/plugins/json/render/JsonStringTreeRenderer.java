package io.github.proton.plugins.json.render;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import io.github.proton.display.Renderer;
import io.github.proton.display.Screen;
import io.github.proton.plugins.json.tree.JsonStringTree;

public final class JsonStringTreeRenderer implements Renderer<JsonStringTree> {
    @Override
    public Screen render(JsonStringTree jsonString, boolean selected) {
        return Screen.from('"' + jsonString.string + '"', x -> new TextCharacter(x, TextColor.ANSI.GREEN, TextColor.ANSI.BLACK));
    }
}
