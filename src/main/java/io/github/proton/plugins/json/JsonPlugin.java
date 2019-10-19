package io.github.proton.plugins.json;

import io.github.proton.display.Renderer;
import io.github.proton.display.Updater;
import io.github.proton.plugins.file.FileOpener;
import io.github.proton.plugins.file.FileType;
import io.github.proton.plugins.json.render.JsonArrayTreeRenderer;
import io.github.proton.plugins.json.render.JsonObjectMemberRenderer;
import io.github.proton.plugins.json.render.JsonObjectTreeRenderer;
import io.github.proton.plugins.json.render.JsonStringTreeRenderer;
import io.github.proton.plugins.json.tree.JsonArrayTree;
import io.github.proton.plugins.json.tree.JsonObjectTree;
import io.github.proton.plugins.json.tree.JsonStringTree;
import io.github.proton.plugins.json.update.JsonArrayTreeUpdater;
import io.github.proton.plugins.json.update.JsonObjectMemberTreeUpdater;
import io.github.proton.plugins.json.update.JsonObjectTreeUpdater;
import io.github.proton.plugins.json.update.JsonStringTreeUpdater;
import io.github.proton.plugins.list.FocusableObservableVerticalRenderer;
import io.github.proton.plugins.list.FocusableObservableVerticalUpdater;
import io.github.proton.util.Registry;

public final class JsonPlugin {
    public void init() {
        FileType.registry.put(new JsonFileType());
        FileOpener.registry.put(JsonFileType.class, new JsonFileOpener());

        Registry<Renderer> rendererRegistry = new Registry<>("json tree renderer");
        @SuppressWarnings("unchecked")
        Renderer<JsonTree> renderer = (jsonTree, selected) -> rendererRegistry.getOrThrow(jsonTree.getClass()).render(jsonTree, selected);
        rendererRegistry.put(JsonObjectTree.class, new JsonObjectTreeRenderer(new FocusableObservableVerticalRenderer<>(new JsonObjectMemberRenderer(renderer))));
        rendererRegistry.put(JsonArrayTree.class, new JsonArrayTreeRenderer(new FocusableObservableVerticalRenderer<>(renderer)));
        rendererRegistry.put(JsonStringTree.class, new JsonStringTreeRenderer());
        Renderer.registry.put(JsonTree.class, renderer);

        Registry<Updater> updaterRegistry = new Registry<>("json tree updater");
        @SuppressWarnings("unchecked")
        Updater.Same<JsonTree> updater = (jsonTree, keyStroke) -> updaterRegistry.getOrThrow(jsonTree.getClass()).update(jsonTree, keyStroke);
        updaterRegistry.put(JsonObjectTree.class, new JsonObjectTreeUpdater(new FocusableObservableVerticalUpdater<>(new JsonObjectMemberTreeUpdater(updater))));
        updaterRegistry.put(JsonArrayTree.class, new JsonArrayTreeUpdater(new FocusableObservableVerticalUpdater<>(updater)));
        updaterRegistry.put(JsonStringTree.class, new JsonStringTreeUpdater());
        Updater.registry.put(JsonTree.class, updater);
    }
}
