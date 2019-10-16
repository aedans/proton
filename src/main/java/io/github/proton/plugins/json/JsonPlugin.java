package io.github.proton.plugins.json;

import io.github.proton.display.Renderer;
import io.github.proton.display.Updater;
import io.github.proton.plugins.file.FileOpener;
import io.github.proton.plugins.file.FileType;
import io.github.proton.plugins.json.render.JsonObjectMemberRenderer;
import io.github.proton.plugins.json.render.JsonObjectTreeRenderer;
import io.github.proton.plugins.json.render.JsonStringTreeRenderer;
import io.github.proton.plugins.json.tree.JsonObjectTree;
import io.github.proton.plugins.json.tree.JsonStringTree;
import io.github.proton.plugins.json.update.JsonObjectMemberTreeUpdater;
import io.github.proton.plugins.json.update.JsonObjectTreeUpdater;
import io.github.proton.plugins.json.update.JsonStringTreeUpdater;
import io.github.proton.plugins.list.FocusableObservableVerticalRenderer;
import io.github.proton.plugins.list.FocusableObservableVerticalUpdater;
import io.github.proton.util.OptionalUpdater;
import io.github.proton.util.Registry;

public final class JsonPlugin {
    public void init() {
        FileType.registry.put(new JsonFileType());
        FileOpener.registry.put(JsonFileType.class, new JsonFileOpener());

        Renderer.registry.put(JsonObjectTree.class, new JsonObjectTreeRenderer(new FocusableObservableVerticalRenderer<>(new JsonObjectMemberRenderer(Renderer.renderer))));
        Renderer.registry.put(JsonStringTree.class, new JsonStringTreeRenderer());

        Registry<Updater> registry = new Registry<>("json tree updater");
        @SuppressWarnings("unchecked")
        Updater.Same<JsonTree> updater = (jsonTree, keyStroke) -> registry.getOrThrow(jsonTree.getClass()).update(jsonTree, keyStroke);
        registry.put(JsonObjectTree.class, new JsonObjectTreeUpdater(new OptionalUpdater<>(new FocusableObservableVerticalUpdater<>(new JsonObjectMemberTreeUpdater(updater)))));
        registry.put(JsonStringTree.class, new JsonStringTreeUpdater());
        Updater.registry.put(JsonTree.class, updater);
    }
}
