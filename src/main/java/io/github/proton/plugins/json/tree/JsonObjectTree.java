package io.github.proton.plugins.json.tree;

import com.eclipsesource.json.JsonObject;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.github.proton.plugins.json.JsonTree;
import io.github.proton.plugins.list.OptionalFocusableObservable;
import io.github.proton.plugins.list.OptionalFocusableObservableComponent;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

public final class JsonObjectTree implements JsonTree {
    public final OptionalFocusableObservable<Component> members;

    public JsonObjectTree(OptionalFocusableObservable<Component> members) {
        this.members = members;
    }

    public static JsonObjectTree from(JsonObject object) {
        Observable<Component> members = Observable.fromIterable(object)
                .map(member -> new JsonObjectMemberTree(member.getName(), JsonTree.from(member.getValue()), false, true));
        return new JsonObjectTree(OptionalFocusableObservable.from(members));
    }

    @Override
    public Maybe<Component> update(KeyStroke keyStroke) {
        return OptionalFocusableObservableComponent.vertical(members).update(keyStroke)
                .map(x -> new JsonArrayTree(x.optional));
    }

    @Override
    public Screen render(boolean selected) {
        Screen open = Screen.from("{", x -> new TextCharacter(x, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        Screen close = Screen.from("}", x -> new TextCharacter(x, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        Screen members = OptionalFocusableObservableComponent.vertical(this.members).render(selected).indent(2);
        if (selected) {
            open = open.inverse();
            close = close.inverse();
        }
        return open.verticalPlus(members).verticalPlus(close);
    }
}
