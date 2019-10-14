package io.github.proton.plugins.json.render;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import io.github.proton.display.Renderer;
import io.github.proton.display.Screen;
import io.github.proton.plugins.json.tree.JsonObjectMemberTree;
import io.github.proton.plugins.json.tree.JsonObjectTree;
import io.github.proton.plugins.list.FocusableObservableRenderer;

public final class JsonObjectTreeRenderer implements Renderer<JsonObjectTree> {
    private final FocusableObservableRenderer<JsonObjectMemberTree> renderer;

    public JsonObjectTreeRenderer(FocusableObservableRenderer<JsonObjectMemberTree> renderer) {
        this.renderer = renderer;
    }

    @Override
    public Screen render(JsonObjectTree tree, boolean selected) {
        Screen open = Screen.from("{", x -> new TextCharacter(x, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        Screen close = Screen.from("}", x -> new TextCharacter(x, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        Screen members = tree.members.map(m -> renderer.render(m, selected).indent(2)).orElse(Screen.empty);
        if (selected) {
            open = open.inverse();
            close = close.inverse();
        }
        return open.verticalPlus(members).verticalPlus(close);
    }
}
