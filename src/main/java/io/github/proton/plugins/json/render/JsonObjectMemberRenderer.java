package io.github.proton.plugins.json.render;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import io.github.proton.display.Renderer;
import io.github.proton.display.Screen;
import io.github.proton.plugins.json.JsonTree;
import io.github.proton.plugins.json.tree.JsonObjectMemberTree;

public final class JsonObjectMemberRenderer implements Renderer<JsonObjectMemberTree> {
    public final Renderer<? super JsonTree> renderer;

    public JsonObjectMemberRenderer(Renderer<? super JsonTree> renderer) {
        this.renderer = renderer;
    }

    @Override
    public Screen render(JsonObjectMemberTree member, boolean selected) {
        Screen name = Screen.from('"' + member.name + '"', x -> new TextCharacter(x, TextColor.ANSI.MAGENTA, TextColor.ANSI.BLACK));
        Screen separator = Screen.from(": ", x -> new TextCharacter(x, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        Screen value = renderer.render(member.value, selected);
        Screen rest = separator.horizontalPlusLeft(value);
        return name.horizontalPlusLeft(selected ? rest.inverse() : rest);
    }
}
