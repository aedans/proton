package io.github.proton.plugins.json;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Updater;
import io.reactivex.rxjava3.core.Single;

public final class JsonTreeUpdater implements Updater.Same<JsonTree> {
    @Override
    public Single<JsonTree> update(JsonTree json, KeyStroke keyStroke) {
        return Single.just(json);
    }
}
