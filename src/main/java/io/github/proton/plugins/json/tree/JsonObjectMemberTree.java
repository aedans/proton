package io.github.proton.plugins.json.tree;

import io.github.proton.plugins.json.JsonTree;

public final class JsonObjectMemberTree {
    public final String name;
    public final JsonTree value;
    public final boolean closed;
    public final boolean focused;

    public JsonObjectMemberTree(String name, JsonTree value, boolean closed, boolean focused) {
        this.name = name;
        this.value = value;
        this.closed = closed;
        this.focused = focused;
    }
}
