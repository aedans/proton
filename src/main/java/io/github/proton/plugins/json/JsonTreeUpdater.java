package io.github.proton.plugins.json;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Updater;
import io.reactivex.rxjava3.core.Maybe;

public final class JsonTreeUpdater implements Updater.Same<JsonTree> {
    @Override
    public Maybe<JsonTree> update(JsonTree json, KeyStroke keyStroke) {
        return Maybe.empty();
    }
}
