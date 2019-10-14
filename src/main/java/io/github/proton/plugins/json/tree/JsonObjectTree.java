package io.github.proton.plugins.json.tree;

import com.eclipsesource.json.JsonObject;
import io.github.proton.plugins.json.JsonTree;
import io.github.proton.plugins.list.FocusableObservable;
import io.reactivex.rxjava3.core.Observable;

import java.util.Optional;

public final class JsonObjectTree implements JsonTree {
    public final Optional<FocusableObservable<JsonObjectMemberTree>> members;

    public JsonObjectTree(Optional<FocusableObservable<JsonObjectMemberTree>> members) {
        this.members = members;
    }

    public static JsonObjectTree from(JsonObject object) {
        if (object.isEmpty()) {
            return new JsonObjectTree(Optional.empty());
        } else {
            Observable<JsonObjectMemberTree> members = Observable.fromIterable(object)
                    .map(member -> new JsonObjectMemberTree(member.getName(), JsonTree.from(member.getValue()), false, true));
            return new JsonObjectTree(Optional.of(new FocusableObservable<>(members)));
        }
    }
}
