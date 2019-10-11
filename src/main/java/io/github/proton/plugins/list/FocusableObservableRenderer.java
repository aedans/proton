package io.github.proton.plugins.list;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import io.github.proton.display.Renderer;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

public final class FocusableObservableRenderer<T> implements Renderer<FocusableObservable<T>> {
    private final Renderer<T> renderer;

    public FocusableObservableRenderer(Renderer<T> renderer) {
        this.renderer = renderer;
    }

    @Override
    public Render render(FocusableObservable<T> list, TerminalPosition position) {
        Observable<TerminalPosition> positions = Observable.range(0, list.length).map(position::withRelativeRow);
        Observable<Render> renders = list.observable().zipWith(positions, renderer::render);
        Observable<Observable<TextCharacter>> screen = renders.flatMap(x -> x.screen);
        Maybe<TerminalPosition> cursor = renders.elementAt(list.index).flatMap(x -> x.cursor);
        return new Render(screen, cursor);
    }
}
