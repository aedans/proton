package io.github.proton.plugins.list;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Updater;

public final class FocusableObservableVerticalUpdater<T> extends FocusableObservableUpdater<T> {
    public FocusableObservableVerticalUpdater(Updater<T, T> updater) {
        super(updater);
    }

    @Override
    protected boolean isNext(KeyStroke keyStroke) {
        return keyStroke.getKeyType() == KeyType.ArrowDown;
    }

    @Override
    protected boolean isPrev(KeyStroke keyStroke) {
        return keyStroke.getKeyType() == KeyType.ArrowUp;
    }
}
