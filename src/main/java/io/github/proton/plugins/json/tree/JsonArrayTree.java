package io.github.proton.plugins.json.tree;

import com.eclipsesource.json.JsonArray;
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

public final class JsonArrayTree implements JsonTree {
    public final OptionalFocusableObservable<Component> elements;

    public JsonArrayTree(OptionalFocusableObservable<Component> elements) {
        this.elements = elements;
    }

    public static JsonArrayTree from(JsonArray array) {
        Observable<Component> observable = Observable.fromIterable(array).map(JsonTree::from);
        return new JsonArrayTree(OptionalFocusableObservable.from(observable));
    }

    @Override
    public Maybe<Component> update(KeyStroke keyStroke) {
        return OptionalFocusableObservableComponent.vertical(elements).update(keyStroke)
                .map(x -> new JsonArrayTree(x.optional));
    }

    @Override
    public Screen render(boolean selected) {
        Screen open = Screen.from("[", x -> new TextCharacter(x, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        Screen close = Screen.from("]", x -> new TextCharacter(x, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        Screen members = OptionalFocusableObservableComponent.vertical(elements).render(selected).indent(2);
        if (selected) {
            open = open.inverse();
            close = close.inverse();
        }
        return open.verticalPlus(members).verticalPlus(close);
    }
}
