package io.github.proton.plugins.list;

import io.reactivex.rxjava3.core.Observable;

public final class FocusableObservable<T> {
    public final Observable<T> before;
    public final Observable<T> after;
    public final T focus;
    public final int length;
    public final int index;

    public FocusableObservable(Observable<T> before, Observable<T> after, T focus, int length, int index) {
        this.before = before;
        this.after = after;
        this.focus = focus;
        this.length = length;
        if (index < 0) {
            this.index = 0;
        } else if (index >= length) {
            this.index = length - 1;
        } else {
            this.index = index;
        }
    }

    public FocusableObservable(Observable<T> objects) {
        this(Observable.empty(), objects.skip(1), objects.blockingFirst(), objects.count().blockingGet().intValue(), 0);
    }

    public Observable<T> observable() {
        return Observable.concat(before, Observable.just(focus), after);
    }
}
