package io.github.proton.plugins.json.tree;

import com.eclipsesource.json.JsonArray;
import io.github.proton.plugins.json.JsonTree;
import io.github.proton.plugins.list.FocusableObservable;
import io.reactivex.rxjava3.core.Observable;

import java.util.Optional;

public final class JsonArrayTree implements JsonTree {
    public final Optional<FocusableObservable<JsonTree>> elements;

    public JsonArrayTree(Optional<FocusableObservable<JsonTree>> elements) {
        this.elements = elements;
    }

    public static JsonArrayTree from(JsonArray array) {
        Observable<JsonTree> observable = Observable.fromIterable(array).map(JsonTree::from);
        return new JsonArrayTree(FocusableObservable.from(observable));
    }
}
