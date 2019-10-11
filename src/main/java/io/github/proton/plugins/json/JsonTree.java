package io.github.proton.plugins.json;

import com.eclipsesource.json.JsonValue;
import io.github.proton.display.Renderer;
import io.github.proton.display.Updater;
import io.github.proton.plugins.file.FileType;

public final class JsonTree {
    static {
        Updater.registry.put(JsonTree.class, new JsonTreeUpdater());
        Renderer.registry.put(JsonTree.class, new JsonTreeRenderer());
        FileType.registry.put(new JsonFileType());
    }

    public final JsonValue object;

    public JsonTree(JsonValue object) {
        this.object = object;
    }
}
