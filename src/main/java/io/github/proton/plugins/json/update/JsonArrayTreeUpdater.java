package io.github.proton.plugins.json.update;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Updater;
import io.github.proton.plugins.json.JsonTree;
import io.github.proton.plugins.json.tree.JsonArrayTree;
import io.github.proton.plugins.list.FocusableObservableUpdater;
import io.github.proton.util.OptionalUpdater;
import io.reactivex.rxjava3.core.Maybe;

public final class JsonArrayTreeUpdater implements Updater.Same<JsonArrayTree> {
    public final FocusableObservableUpdater<JsonTree> updater;

    public JsonArrayTreeUpdater(FocusableObservableUpdater<JsonTree> updater) {
        this.updater = updater;
    }

    @Override
    public Maybe<JsonArrayTree> update(JsonArrayTree tree, KeyStroke keyStroke) {
        return new OptionalUpdater<>(updater).update(tree.elements, keyStroke).map(JsonArrayTree::new);

    }
}
