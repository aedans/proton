package io.github.proton.plugins.json.update;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.plugins.json.JsonTree;
import io.github.proton.plugins.json.tree.JsonObjectMemberTree;
import io.github.proton.plugins.json.tree.JsonObjectTree;
import io.github.proton.plugins.list.FocusableObservableUpdater;
import io.reactivex.rxjava3.core.Maybe;

public final class JsonObjectTreeUpdater implements JsonTreeUpdater<JsonObjectTree> {
    public final FocusableObservableUpdater<JsonObjectMemberTree> updater;

    public JsonObjectTreeUpdater(FocusableObservableUpdater<JsonObjectMemberTree> updater) {
        this.updater = updater;
    }

    @Override
    public Maybe<JsonTree> update(JsonObjectTree jsonObject, KeyStroke keyStroke) {
        Maybe<JsonTree> otherwise = Maybe.empty();
        if (jsonObject.closed && keyStroke.getKeyType() == KeyType.ArrowRight)
            otherwise = Maybe.just(new JsonObjectTree(jsonObject.members, false));
        if (!jsonObject.closed && keyStroke.getKeyType() == KeyType.ArrowLeft)
            otherwise = Maybe.just(new JsonObjectTree(jsonObject.members, true));

        Maybe<JsonTree> tree = Maybe.empty();
        if (!jsonObject.closed && jsonObject.members != null)
            tree = updater.update(jsonObject.members, keyStroke)
                    .map(members -> new JsonObjectTree(members, false));

        return tree.switchIfEmpty(otherwise);
    }
}
