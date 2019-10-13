package io.github.proton.plugins.json.tree;

import io.github.proton.plugins.json.JsonTree;

public final class JsonObjectMemberTree {
    public final String name;
    public final JsonTree value;

    public JsonObjectMemberTree(String name, JsonTree value) {
        this.name = name;
        this.value = value;
    }
}
