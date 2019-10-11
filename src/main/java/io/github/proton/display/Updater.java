package io.github.proton.display;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.util.Registry;

public interface Updater<T> {
    Registry<Updater> registry = new Registry<>("updater");
    @SuppressWarnings("unchecked")
    Updater<Object> updater = (o, keyStroke) -> registry.get(o.getClass()).update(o, keyStroke);

    T update(T t, KeyStroke keyStroke);
}
