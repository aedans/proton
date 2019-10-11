package io.github.proton.plugins.list;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Updater;

public abstract class FocusableObservableUpdater<T> implements Updater<FocusableObservable<T>> {
    private final Updater<T> updater;

    public FocusableObservableUpdater(Updater<T> updater) {
        this.updater = updater;
    }

    protected abstract boolean isNext(KeyStroke keyStroke);

    protected abstract boolean isPrev(KeyStroke keyStroke);

    @Override
    public FocusableObservable<T> update(FocusableObservable<T> observable, KeyStroke keyStroke) {
        if (isNext(keyStroke))
            return observable.next();
        if (isPrev(keyStroke))
            return observable.prev();
        return new FocusableObservable<>(
                observable.before,
                observable.after,
                observable.focus.map(focus -> updater.update(focus, keyStroke))
        );
    }
}
