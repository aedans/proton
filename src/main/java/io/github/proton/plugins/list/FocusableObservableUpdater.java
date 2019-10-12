package io.github.proton.plugins.list;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Updater;
import io.reactivex.rxjava3.core.Single;

public abstract class FocusableObservableUpdater<T> implements Updater.Same<FocusableObservable<T>> {
    private final Updater.Same<T> updater;

    public FocusableObservableUpdater(Updater.Same<T> updater) {
        this.updater = updater;
    }

    protected abstract boolean isNext(KeyStroke keyStroke);

    protected abstract boolean isPrev(KeyStroke keyStroke);

    @Override
    public Single<FocusableObservable<T>> update(FocusableObservable<T> observable, KeyStroke keyStroke) {
        if (isNext(keyStroke))
            return Single.just(observable.next());
        if (isPrev(keyStroke))
            return Single.just(observable.prev());
        return observable.focus.map(focus -> updater.update(focus, keyStroke))
                .map(newFocus -> new FocusableObservable<>(observable.before, observable.after, newFocus));
    }
}
