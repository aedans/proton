package io.github.proton.plugins.json.update;

import io.github.proton.display.Updater;
import io.github.proton.plugins.json.JsonTree;
import io.github.proton.util.Registry;

public interface JsonTreeUpdater<T> extends Updater<T, JsonTree> {
    Registry<JsonTreeUpdater> registry = new Registry<>("json tree updater");
    @SuppressWarnings("unchecked")
    JsonTreeUpdater<JsonTree> updater = (jsonTree, keyStroke) -> registry.getOrThrow(jsonTree.getClass()).update(jsonTree, keyStroke);
}
