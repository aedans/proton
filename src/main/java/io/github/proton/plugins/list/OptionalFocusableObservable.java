package io.github.proton.plugins.list;

import io.reactivex.rxjava3.core.Observable;

import java.util.Optional;

public final class OptionalFocusableObservable<T> {
    public final Optional<FocusableObservable<T>> optionalObservable;

    public OptionalFocusableObservable(Optional<FocusableObservable<T>> optionalObservable) {
        this.optionalObservable = optionalObservable;
    }

    public static <T> OptionalFocusableObservable<T> from(Observable<T> observable) {
        return new OptionalFocusableObservable<>(FocusableObservable.from(observable));
    }

    public Observable<T> observable() {
        return optionalObservable.map(FocusableObservable::observable).orElse(Observable.empty());
    }
}
