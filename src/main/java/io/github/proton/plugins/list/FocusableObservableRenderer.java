package io.github.proton.plugins.list;

import io.github.proton.display.Renderer;
import io.github.proton.display.Screen;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public abstract class FocusableObservableRenderer<T> implements Renderer<FocusableObservable<T>> {
    protected final Renderer<T> renderer;

    public FocusableObservableRenderer(Renderer<T> renderer) {
        this.renderer = renderer;
    }

    protected abstract Screen combine(Screen a, Screen b);

    @Override
    public Screen render(FocusableObservable<T> list, boolean selected) {
        Observable<Screen> beforeScreens = list.before.map(x -> renderer.render(x, false));
        Observable<Screen> afterScreens = list.after.map(x -> renderer.render(x, false));
        Single<Screen> focusScreen = list.focus.map(focus -> {
            Screen render = renderer.render(focus, selected);
            return selected ? render.inverse() : render;
        });
        Observable<Screen> screens = Observable.concat(beforeScreens, focusScreen.toObservable(), afterScreens);
        return screens.reduce(Screen.empty, this::combine).blockingGet();
    }
}
