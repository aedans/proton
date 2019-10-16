package io.github.proton.plugins.json.update;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Updater;
import io.github.proton.plugins.json.JsonTree;
import io.github.proton.plugins.json.tree.JsonObjectMemberTree;
import io.reactivex.rxjava3.core.Maybe;

public final class JsonObjectMemberTreeUpdater implements Updater.Same<JsonObjectMemberTree> {
    public final Updater.Same<JsonTree> updater;

    public JsonObjectMemberTreeUpdater(Updater.Same<JsonTree> updater) {
        this.updater = updater;
    }

    @Override
    public Maybe<JsonObjectMemberTree> update(JsonObjectMemberTree jsonObjectMember, KeyStroke keyStroke) {
        if (jsonObjectMember.focused) {
            if (keyStroke.getKeyType() == KeyType.ArrowRight) {
                return Maybe.just(new JsonObjectMemberTree(jsonObjectMember.name, jsonObjectMember.value, false, true));
            }
            if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
                return Maybe.just(new JsonObjectMemberTree(jsonObjectMember.name, jsonObjectMember.value, true, true));
            }
            if (keyStroke.getKeyType() == KeyType.ArrowDown && !jsonObjectMember.closed) {
                return Maybe.just(new JsonObjectMemberTree(jsonObjectMember.name, jsonObjectMember.value, false, false));
            }
        }
        if (!jsonObjectMember.focused && !jsonObjectMember.closed) {
            Maybe<JsonObjectMemberTree> maybe = updater.update(jsonObjectMember.value, keyStroke)
                    .map(value -> new JsonObjectMemberTree(jsonObjectMember.name, value, false, false));
            if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                maybe = maybe.defaultIfEmpty(new JsonObjectMemberTree(jsonObjectMember.name, jsonObjectMember.value, false, true)).toMaybe();
            }
            return maybe;
        }

        return Maybe.empty();
    }
}
