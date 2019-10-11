package io.github.proton.plugins.list;

import com.googlecode.lanterna.TextCharacter;
import io.github.proton.display.Renderer;
import io.github.proton.display.Screen;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public final class FocusableObservableRenderer<T> implements Renderer<FocusableObservable<T>> {
    private final Renderer<T> renderer;

    public FocusableObservableRenderer(Renderer<T> renderer) {
        this.renderer = renderer;
    }

    @Override
    public Screen render(FocusableObservable<T> list) {
        Observable<Screen> beforeScreens = list.before.map(renderer::render);
        Observable<Screen> afterScreens = list.after.map(renderer::render);
        Single<Screen> focusScreen = list.focus.map(focus -> renderer.render(focus).inverse());
        Observable<Screen> screens = Observable.concat(beforeScreens, focusScreen.toObservable(), afterScreens);
        Observable<Observable<TextCharacter>> screen = screens.flatMap(x -> x.chars);
        return new Screen(screen);
    }
}
