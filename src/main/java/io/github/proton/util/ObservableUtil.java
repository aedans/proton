package io.github.proton.util;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public final class ObservableUtil {
    private ObservableUtil() {
    }

    public static Single<Long> max(Single<Long> a, Single<Long> b) {
        return a.flatMap(a1 -> b.map(b1 -> Math.max(a1, b1)));
    }

    public static <T> Observable<T> removeAt(Observable<T> observable, long index) {
        if (index == 0) {
            return observable.skip(1);
        } else {
            return Observable.concat(observable.take(index - 1), observable.skip(index));
        }
    }

    public static Observable<Character> fromString(String string) {
        return Observable.create(emitter -> {
            for (int i = 0; i < string.length(); ++i) {
                emitter.onNext(string.charAt(i));
            }
            emitter.onComplete();
        });
    }
}
