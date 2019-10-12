package io.github.proton.display;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.util.Registry;
import io.reactivex.rxjava3.core.Single;

public interface Updater<T, A> {
    Registry<Updater> registry = new Registry<>("updater");
    @SuppressWarnings("unchecked")
    Updater.Same<Object> updater = (o, keyStroke) -> registry.get(o.getClass()).update(o, keyStroke);

    Single<A> update(T t, KeyStroke keyStroke);

    interface Same<T> extends Updater<T, T> {

    }
}
