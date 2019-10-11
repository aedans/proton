package io.github.proton.plugins.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.proton.display.Renderer;
import io.github.proton.display.Updater;

public final class JsonTree {
    static {
        Updater.registry.put(JsonTree.class, new JsonTreeUpdater());
        Renderer.registry.put(JsonTree.class, new JsonTreeRenderer());
    }

    public final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public final Object object;

    public JsonTree(Object object) {
        this.object = object;
    }
}
