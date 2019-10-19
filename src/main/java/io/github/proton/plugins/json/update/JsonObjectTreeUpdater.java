package io.github.proton.plugins.json.update;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Updater;
import io.github.proton.plugins.json.tree.JsonObjectMemberTree;
import io.github.proton.plugins.json.tree.JsonObjectTree;
import io.github.proton.plugins.list.FocusableObservableUpdater;
import io.github.proton.util.OptionalUpdater;
import io.reactivex.rxjava3.core.Maybe;

public final class JsonObjectTreeUpdater implements Updater.Same<JsonObjectTree> {
    public final FocusableObservableUpdater<JsonObjectMemberTree> updater;

    public JsonObjectTreeUpdater(FocusableObservableUpdater<JsonObjectMemberTree> updater) {
        this.updater = updater;
    }

    @Override
    public Maybe<JsonObjectTree> update(JsonObjectTree jsonObject, KeyStroke keyStroke) {
        return new OptionalUpdater<>(updater).update(jsonObject.members, keyStroke).map(JsonObjectTree::new);
    }
}
