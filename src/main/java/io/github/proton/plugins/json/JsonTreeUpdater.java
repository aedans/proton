package io.github.proton.plugins.json;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Updater;

public final class JsonTreeUpdater implements Updater.Same<JsonTree> {
    @Override
    public JsonTree update(JsonTree json, KeyStroke keyStroke) {
        return json;
    }
}
