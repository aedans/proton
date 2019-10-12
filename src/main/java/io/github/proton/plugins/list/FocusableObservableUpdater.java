package io.github.proton.plugins.list;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Updater;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public abstract class FocusableObservableUpdater<T> implements Updater.Same<FocusableObservable<T>> {
    private final Updater.Same<T> updater;

    public FocusableObservableUpdater(Updater.Same<T> updater) {
        this.updater = updater;
    }

    protected abstract boolean isNext(KeyStroke keyStroke);

    protected abstract boolean isPrev(KeyStroke keyStroke);

    @Override
    public Maybe<FocusableObservable<T>> update(FocusableObservable<T> observable, KeyStroke keyStroke) {
        Maybe<FocusableObservable<T>> otherwise = Maybe.empty();
        if (isNext(keyStroke))
            otherwise = observable.after.isEmpty()
                    .flatMapMaybe(isEmpty -> isEmpty ? Maybe.empty() : Maybe.just(observable.next()));
        if (isPrev(keyStroke))
            otherwise = observable.before.isEmpty()
                    .flatMapMaybe(isEmpty -> isEmpty ? Maybe.empty() : Maybe.just(observable.prev()));
        return observable.focus
                .flatMapMaybe(focus -> updater.update(focus, keyStroke))
                .map(newFocus -> new FocusableObservable<>(observable.before, observable.after, Single.just(newFocus)))
                .switchIfEmpty(otherwise);
    }
}
