package io.github.proton.display.components;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.github.proton.util.ObservableUtil;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public final class ListComponent implements Component {
    private final Observable<Component> components;

    private ListComponent(Observable<Component> components) {
        this.components = components;
    }

    public static ListComponent of(String string) {
        return of(ObservableUtil.fromString(string).map(CharacterComponent::of));
    }

    public static ListComponent of(Observable<Component> components) {
        return new ListComponent(components);
    }

    @Override
    public Maybe<Component> update(KeyStroke keyStroke) {
        return Maybe.empty();
    }

    @Override
    public Single<Screen> render(boolean selected) {
        return components.flatMapSingle(x -> x.render(selected)).reduce(Screen.empty, Screen::horizontalPlus);
    }
}
