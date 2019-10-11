package io.github.proton.plugins.list;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Updater;

public final class FocusableObservableHorizontalUpdater<T> extends FocusableObservableUpdater<T> {
    public FocusableObservableHorizontalUpdater(Updater.Same<T> updater) {
        super(updater);
    }

    @Override
    protected boolean isNext(KeyStroke keyStroke) {
        return keyStroke.getKeyType() == KeyType.ArrowLeft;
    }

    @Override
    protected boolean isPrev(KeyStroke keyStroke) {
        return keyStroke.getKeyType() == KeyType.ArrowRight;
    }
}
