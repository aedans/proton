package io.github.proton.plugins.json.render;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import io.github.proton.display.Renderer;
import io.github.proton.display.Screen;
import io.github.proton.plugins.json.JsonTree;
import io.github.proton.plugins.json.tree.JsonArrayTree;
import io.github.proton.plugins.list.FocusableObservableRenderer;

public final class JsonArrayTreeRenderer implements Renderer<JsonArrayTree> {
    public final FocusableObservableRenderer<JsonTree> renderer;

    public JsonArrayTreeRenderer(FocusableObservableRenderer<JsonTree> renderer) {
        this.renderer = renderer;
    }

    @Override
    public Screen render(JsonArrayTree array, boolean selected) {
        Screen open = Screen.from("[", x -> new TextCharacter(x, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        Screen close = Screen.from("]", x -> new TextCharacter(x, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        Screen members = array.elements.map(m -> renderer.render(m, selected).indent(2)).orElse(Screen.empty);
        if (selected) {
            open = open.inverse();
            close = close.inverse();
        }
        return open.verticalPlus(members).verticalPlus(close);
    }
}
