package io.github.proton.plugins.json.update;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Updater;
import io.github.proton.plugins.json.JsonTree;
import io.github.proton.plugins.json.tree.JsonObjectMemberTree;
import io.reactivex.rxjava3.core.Maybe;

public final class JsonObjectMemberTreeUpdater implements Updater.Same<JsonObjectMemberTree> {
    public final JsonTreeUpdater<JsonTree> updater;

    public JsonObjectMemberTreeUpdater(JsonTreeUpdater<JsonTree> updater) {
        this.updater = updater;
    }

    @Override
    public Maybe<JsonObjectMemberTree> update(JsonObjectMemberTree jsonObjectMember, KeyStroke keyStroke) {
        return updater.update(jsonObjectMember.value, keyStroke).map(x -> new JsonObjectMemberTree(jsonObjectMember.name, x));
    }
}
