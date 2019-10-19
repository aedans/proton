package io.github.proton.plugins.list;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public final class FocusableObservableComponent implements Component {
    public final FocusableObservable<Component> observable;
    public final Predicate<KeyStroke> isNext;
    public final Predicate<KeyStroke> isPrev;
    public final BinaryOperator<Screen> combine;

    public FocusableObservableComponent(
            FocusableObservable<Component> observable,
            Predicate<KeyStroke> isNext,
            Predicate<KeyStroke> isPrev,
            BinaryOperator<Screen> combine
    ) {
        this.observable = observable;
        this.isNext = isNext;
        this.isPrev = isPrev;
        this.combine = combine;
    }

    public static FocusableObservableComponent vertical(FocusableObservable<Component> observable) {
        return new FocusableObservableComponent(
                observable,
                x -> x.getKeyType() == KeyType.ArrowDown,
                x -> x.getKeyType() == KeyType.ArrowUp,
                Screen::verticalPlus
        );
    }

    public static FocusableObservableComponent horizontal(FocusableObservable<Component> observable) {
        return new FocusableObservableComponent(
                observable,
                x -> x.getKeyType() == KeyType.ArrowRight,
                x -> x.getKeyType() == KeyType.ArrowLeft,
                Screen::horizontalPlus
        );
    }

    @Override
    public Maybe<FocusableObservableComponent> update(KeyStroke keyStroke) {
        Maybe<FocusableObservable<Component>> otherwise = Maybe.empty();
        if (isNext.test(keyStroke))
            otherwise = observable.after.isEmpty()
                    .flatMapMaybe(isEmpty -> isEmpty ? Maybe.empty() : Maybe.just(observable.next()));
        if (isPrev.test(keyStroke))
            otherwise = observable.before.isEmpty()
                    .flatMapMaybe(isEmpty -> isEmpty ? Maybe.empty() : Maybe.just(observable.prev()));
        return observable.focus
                .flatMapMaybe(focus -> focus.update(keyStroke))
                .map(newFocus -> new FocusableObservable<>(observable.before, observable.after, Single.just(newFocus)))
                .switchIfEmpty(otherwise)
                .map(x -> new FocusableObservableComponent(x, isNext, isPrev, combine));
    }

    @Override
    public Screen render(boolean selected) {
        Observable<Screen> beforeScreens = observable.before.map(x -> x.render(false));
        Observable<Screen> afterScreens = observable.after.map(x -> x.render(false));
        Single<Screen> focusScreen = observable.focus.map(focus -> focus.render(selected));
        Observable<Screen> screens = Observable.concat(beforeScreens, focusScreen.toObservable(), afterScreens);
        return screens.reduce(combine::apply).blockingGet();
    }
}
