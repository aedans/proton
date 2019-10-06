package io.github.proton.util;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Predicate;

public class ObservableUtil {
    public static <T> Observable<Observable<T>> split(Observable<T> observable, Predicate<? super T> predicate) {
        return Observable.create(emitter -> {
            Observable<T> observable1 = observable;
            while (!observable1.isEmpty().blockingGet()) {
                Observable<T> value = observable1.takeWhile(x -> !predicate.test(x));
                emitter.onNext(value);
                observable1 = observable1.skip(value.count().blockingGet() + 1);
            }
            emitter.onComplete();
        });
    }
}
