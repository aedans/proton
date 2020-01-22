package io.github.proton.display.editors;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Editor;
import io.github.proton.display.Screen;
import io.github.proton.display.views.ListView;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.function.BinaryOperator;

public final class ListEditor<T, E> implements Editor<T> {
    private final BinaryOperator<Screen> combine;
    private final Observable<Editor<E>> before;
    private final Observable<Editor<E>> after;
    private final Editor<E> focus;
    private final ListView<T> view;
    private final T list;

    private ListEditor(
            BinaryOperator<Screen> combine,
            ListView<T> view,
            Observable<Editor<E>> before,
            Observable<Editor<E>> after,
            Editor<E> focus,
            T list) {
        this.before = before;
        this.after = after;
        this.focus = focus;
        this.combine = combine;
        this.view = view;
        this.list = list;
    }

    public static <T, E> Maybe<ListEditor<T, E>> of(
            BinaryOperator<Screen> combine,
            Observable<Editor<E>> components,
            ListView<T> view,
            T list) {
        return components.firstElement().map(focus ->
                new ListEditor<>(combine, view, Observable.empty(), components.skip(1), focus, list));
    }

    @Override
    public Maybe<? extends Editor<T>> update(KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            return before.lastElement().map(newFocus -> new ListEditor<>(
                    combine,
                    view,
                    before.skipLast(1),
                    Observable.concat(Observable.just(focus), after),
                    newFocus,
                    list));
        }
        if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            return after.firstElement().map(newFocus -> new ListEditor<>(
                    combine,
                    view,
                    Observable.concat(before, Observable.just(focus)),
                    after.skip(1),
                    newFocus,
                    list));
        }
        if (keyStroke.getKeyType() == KeyType.Backspace) {
            return before.count().map(index ->
                    new ListEditor<>(
                            combine,
                            view,
                            before.skipLast(1),
                            after,
                            focus,
                            view.removeAt(list, index)))
                    .toMaybe();
        }
        return Maybe.empty();
    }

    @Override
    public Single<Screen> render(boolean selected) {
        Observable<Screen> before = this.before.flatMapSingle(x -> x.render(false));
        Observable<Screen> after = this.after.flatMapSingle(x -> x.render(false));
        Single<Screen> focus = this.focus.render(selected);
        return Observable.concat(before, focus.toObservable(), after).reduce(Screen.empty, combine::apply);
    }

    @Override
    public T get() {
        return list;
    }
}
