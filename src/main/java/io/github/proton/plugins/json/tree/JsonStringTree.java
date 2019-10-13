package io.github.proton.plugins.json.tree;

import io.github.proton.plugins.json.JsonTree;

public final class JsonStringTree implements JsonTree {
    public final String string;

    public JsonStringTree(String string) {
        this.string = string;
    }

    public static JsonStringTree from(String string) {
        return new JsonStringTree(string);
    }
}
