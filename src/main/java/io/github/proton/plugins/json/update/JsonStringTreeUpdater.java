package io.github.proton.plugins.json.update;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Updater;
import io.github.proton.plugins.json.JsonTree;
import io.github.proton.plugins.json.tree.JsonStringTree;
import io.reactivex.rxjava3.core.Maybe;

public final class JsonStringTreeUpdater implements Updater<JsonStringTree, JsonTree> {
    @Override
    public Maybe<JsonTree> update(JsonStringTree jsonStringTree, KeyStroke keyStroke) {
        return Maybe.empty();
    }
}
