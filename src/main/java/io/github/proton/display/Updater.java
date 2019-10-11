package io.github.proton.display;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.util.Registry;

public interface Updater<T, A> {
    Registry<Updater> registry = new Registry<>("updater");
    @SuppressWarnings("unchecked")
    Updater.Same<Object> updater = (o, keyStroke) -> registry.get(o.getClass()).update(o, keyStroke);

    A update(T t, KeyStroke keyStroke);

    interface Same<T> extends Updater<T, T> {

    }
}
