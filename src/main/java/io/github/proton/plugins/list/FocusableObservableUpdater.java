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
            return new FocusableObservable<>(observable.before, observable.after, observable.focus, observable.length, observable.index + 1);
        if (keyStroke.getKeyType() == KeyType.ArrowUp)
            return new FocusableObservable<>(observable.before, observable.after, observable.focus, observable.length, observable.index - 1);
        return new FocusableObservable<>(observable.before, observable.after, updater.update(observable.focus, keyStroke), observable.length, observable.index);
    }
}
