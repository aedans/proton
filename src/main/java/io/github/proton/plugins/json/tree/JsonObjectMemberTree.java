package io.github.proton.plugins.json.tree;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.reactivex.rxjava3.core.Maybe;

public final class JsonObjectMemberTree implements Component {
    public final String name;
    public final Component value;
    public final boolean closed;
    public final boolean focused;

    public JsonObjectMemberTree(String name, Component value, boolean closed, boolean focused) {
        this.name = name;
        this.value = value;
        this.closed = closed;
        this.focused = focused;
    }

    @Override
    public Maybe<Component> update(KeyStroke keyStroke) {
        if (focused) {
            if (keyStroke.getKeyType() == KeyType.ArrowRight) {
                return Maybe.just(new JsonObjectMemberTree(name, value, false, true));
            }
            if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
                return Maybe.just(new JsonObjectMemberTree(name, value, true, true));
            }
            if (keyStroke.getKeyType() == KeyType.ArrowDown && !closed) {
                return Maybe.just(new JsonObjectMemberTree(name, value, false, false));
            }
        }
        if (!focused && !closed) {
            Maybe<Component> maybe = value.update(keyStroke)
                    .map(value -> new JsonObjectMemberTree(name, value, false, false));
            if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                maybe = maybe.defaultIfEmpty(new JsonObjectMemberTree(name, value, false, true)).toMaybe();
            }
            return maybe;
        }

        return Maybe.empty();
    }

    @Override
    public Screen render(boolean selected) {
        Screen name = Screen.from('"' + this.name + '"', x -> new TextCharacter(x, TextColor.ANSI.MAGENTA, TextColor.ANSI.BLACK));
        Screen separator = Screen.from(": ", x -> new TextCharacter(x, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        Screen value = closed
                ? Screen.from("...", x -> new TextCharacter(x, TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK))
                : this.value.render(selected && !focused);
        Screen rest = separator.horizontalPlusLeft(value);
        if (selected && focused) {
            name = name.inverse();
        }
        return name.horizontalPlusLeft(rest);
    }
}
