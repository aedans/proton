package io.github.proton.plugins.list;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Updater;

public final class FocusableObservableUpdater<T> implements Updater<FocusableObservable<T>> {
    private final Updater<T> updater;

    public FocusableObservableUpdater(Updater<T> updater) {
        this.updater = updater;
    }

    @Override
    public FocusableObservable<T> update(FocusableObservable<T> observable, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.ArrowDown)
            return observable.next();
        if (keyStroke.getKeyType() == KeyType.ArrowUp)
            return observable.prev();
        return new FocusableObservable<>(
                observable.before,
                observable.after,
                observable.focus.map(focus -> updater.update(focus, keyStroke))
        );
    }
}
