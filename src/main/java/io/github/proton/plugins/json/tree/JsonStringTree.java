package io.github.proton.plugins.json.tree;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.github.proton.plugins.json.JsonTree;
import io.reactivex.rxjava3.core.Maybe;

public final class JsonStringTree implements JsonTree {
    public final String string;

    public JsonStringTree(String string) {
        this.string = string;
    }

    public static JsonStringTree from(String string) {
        return new JsonStringTree(string);
    }

    @Override
    public Maybe<Component> update(KeyStroke keyStroke) {
        return Maybe.empty();
    }

    @Override
    public Screen render(boolean selected) {
        Screen screen = Screen.from('"' + string + '"', x -> new TextCharacter(x, TextColor.ANSI.GREEN, TextColor.ANSI.BLACK));
        return selected ? screen.inverse() : screen;
    }
}
