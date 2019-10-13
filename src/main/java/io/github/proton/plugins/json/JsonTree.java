package io.github.proton.plugins.json;

import com.eclipsesource.json.JsonValue;
import io.github.proton.plugins.json.tree.JsonObjectTree;
import io.github.proton.plugins.json.tree.JsonStringTree;

public interface JsonTree {
    static JsonTree from(JsonValue value) {
        if (value.isObject()) {
            return JsonObjectTree.from(value.asObject());
        } else if (value.isString()) {
            return JsonStringTree.from(value.asString());
        } else {
            throw new RuntimeException(value + " cannot be converted to a json tree");
        }
    }
}
