package io.github.proton.util;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Updater;
import io.reactivex.rxjava3.core.Maybe;

import java.util.Optional;

public final class OptionalUpdater<T> implements Updater.Same<Optional<T>> {
    public final Updater.Same<T> updater;

    public OptionalUpdater(Same<T> updater) {
        this.updater = updater;
    }

    @Override
    public Maybe<Optional<T>> update(Optional<T> t, KeyStroke keyStroke) {
        if (t.isPresent()) {
            return updater.update(t.get(), keyStroke).map(Optional::of);
        } else {
            return Maybe.empty();
        }
    }
}
