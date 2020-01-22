package io.github.proton.display.components;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.github.proton.util.ObservableUtil;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public final class ListComponent implements Component {
    private final Observable<Component> before;
    private final Observable<Component> after;
    private final Component focus;

    private ListComponent(Observable<Component> before, Observable<Component> after, Component focus) {
        this.before = before;
        this.after = after;
        this.focus = focus;
    }

    public static ListComponent of(String string) {
        return of(ObservableUtil.fromString(string).map(CharacterComponent::of), CharacterComponent.of(' '));
    }

    public static ListComponent of(Observable<Component> components, Component tail) {
        return of(Observable.concat(components, Observable.just(tail))).blockingGet();
    }

    public static Maybe<ListComponent> of(Observable<Component> components) {
        return components.firstElement().map(focus -> of(focus, components.skip(1)));
    }

    public static ListComponent of(Component head, Observable<Component> components) {
        return new ListComponent(Observable.empty(), components, head);
    }

    @Override
    public Maybe<Component> update(KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            return before.lastElement().map(newFocus ->
                    new ListComponent(before.skipLast(1), Observable.concat(Observable.just(focus), after), newFocus));
        }
        if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            return after.firstElement().map(newFocus ->
                    new ListComponent(Observable.concat(before, Observable.just(focus)), after.skip(1), newFocus));
        }
        return Maybe.empty();
    }

    @Override
    public Single<Screen> render(boolean selected) {
        Single<Screen> before = this.before.flatMapSingle(x -> x.render(false)).reduce(Screen.empty, Screen::horizontalPlus);
        Single<Screen> after = this.after.flatMapSingle(x -> x.render(false)).reduce(Screen.empty, Screen::horizontalPlus);
        Single<Screen> focus = this.focus.render(selected);
        return before.flatMap(b -> after.flatMap(a -> focus.map(f -> b.horizontalPlus(f).horizontalPlus(a))));
    }
}
