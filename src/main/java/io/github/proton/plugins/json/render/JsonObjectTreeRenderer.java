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
        if (tree.closed)
            return Screen.from("{...}", x -> new TextCharacter(x, TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK));

        Screen open = Screen.from("{", x -> new TextCharacter(x, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        Screen close = Screen.from("}", x -> new TextCharacter(x, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        Screen members = tree.members != null ? renderer.render(tree.members, selected).indent(2) : Screen.empty;
        return open.verticalPlus(members).verticalPlus(close);
    }
}
