package io.github.proton.plugins.json.render;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import io.github.proton.display.Renderer;
import io.github.proton.display.Screen;
import io.github.proton.plugins.json.JsonTree;
import io.github.proton.plugins.json.tree.JsonObjectMemberTree;

public final class JsonObjectMemberRenderer implements Renderer<JsonObjectMemberTree> {
    public final Renderer<JsonTree> renderer;

    public JsonObjectMemberRenderer(Renderer<JsonTree> renderer) {
        this.renderer = renderer;
    }

    @Override
    public Screen render(JsonObjectMemberTree member, boolean selected) {
        Screen name = Screen.from('"' + member.name + '"', x -> new TextCharacter(x, TextColor.ANSI.MAGENTA, TextColor.ANSI.BLACK));
        Screen separator = Screen.from(": ", x -> new TextCharacter(x, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        Screen value = member.closed
                ? Screen.from("{...}", x -> new TextCharacter(x, TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK))
                : renderer.render(member.value, selected && !member.focused);
        Screen rest = separator.horizontalPlusLeft(value);
        if (selected && member.focused) {
            name = name.inverse();
        }
        return name.horizontalPlusLeft(rest);
    }
}
