package io.github.proton.plugins.json.tree;

import com.eclipsesource.json.JsonObject;
import io.github.proton.plugins.json.JsonTree;
import io.github.proton.plugins.list.FocusableObservable;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Observable;

public final class JsonObjectTree implements JsonTree {
    @Nullable
    public final FocusableObservable<JsonObjectMemberTree> members;
    public final boolean closed;

    public JsonObjectTree(@Nullable FocusableObservable<JsonObjectMemberTree> members, boolean closed) {
        this.members = members;
        this.closed = closed;
    }

    public static JsonObjectTree from(JsonObject object) {
        if (object.isEmpty()) {
            return new JsonObjectTree(null, false);
        } else {
            Observable<JsonObjectMemberTree> members = Observable.fromIterable(object)
                    .map(member -> new JsonObjectMemberTree(member.getName(), JsonTree.from(member.getValue())));
            return new JsonObjectTree(new FocusableObservable<>(members), false);
        }
    }
}
