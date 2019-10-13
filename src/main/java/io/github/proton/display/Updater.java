package io.github.proton.display;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.util.Registry;
import io.reactivex.rxjava3.core.Maybe;

public interface Updater<T, A> {
    Registry<Updater> registry = new Registry<>("updater");
    @SuppressWarnings("unchecked")
    Updater.Same<Object> updater = (o, keyStroke) -> registry.getOrThrow(o.getClass()).update(o, keyStroke);

    Maybe<A> update(T t, KeyStroke keyStroke);

    interface Same<T> extends Updater<T, T> {

    }
}
