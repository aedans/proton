package io.github.proton.plugins.json.update;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Updater;
import io.github.proton.plugins.json.JsonTree;
import io.github.proton.plugins.json.tree.JsonObjectMemberTree;
import io.github.proton.plugins.json.tree.JsonObjectTree;
import io.github.proton.plugins.list.FocusableObservable;
import io.reactivex.rxjava3.core.Maybe;

import java.util.Optional;

public final class JsonObjectTreeUpdater implements JsonTreeUpdater<JsonObjectTree> {
    public final Updater.Same<Optional<FocusableObservable<JsonObjectMemberTree>>> updater;

    public JsonObjectTreeUpdater(Updater.Same<Optional<FocusableObservable<JsonObjectMemberTree>>> updater) {
        this.updater = updater;
    }

    @Override
    public Maybe<JsonTree> update(JsonObjectTree jsonObject, KeyStroke keyStroke) {
        return updater.update(jsonObject.members, keyStroke).map(JsonObjectTree::new);
    }
}
