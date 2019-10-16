package io.github.proton.plugins.list;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public final class FocusableObservable<T> {
    public final Observable<T> before;
    public final Observable<T> after;
    public final Single<T> focus;

    public FocusableObservable(Observable<T> before, Observable<T> after, Single<T> focus) {
        this.before = before;
        this.after = after;
        this.focus = focus;
    }

    public FocusableObservable(Observable<T> objects) {
        this(Observable.empty(), objects.skip(1), objects.firstOrError());
    }

    public Observable<T> observable() {
        return Observable.concat(before, focus.toObservable(), after);
    }

    public FocusableObservable<T> next() {
        if (after.isEmpty().blockingGet()) {
            return this;
        } else {
            return new FocusableObservable<>(
                    Observable.concat(before, focus.toObservable()).cache(),
                    after.skip(1).cache(),
                    after.firstOrError()
            );
        }
    }

    public FocusableObservable<T> prev() {
        if (before.isEmpty().blockingGet()) {
            return this;
        } else {
            return new FocusableObservable<>(
                    before.skipLast(1).cache(),
                    Observable.concat(focus.toObservable(), after).cache(),
                    before.lastOrError()
            );
        }
    }
}
