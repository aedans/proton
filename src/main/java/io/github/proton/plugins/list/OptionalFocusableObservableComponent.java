package io.github.proton.plugins.list;

import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.reactivex.rxjava3.core.Maybe;

import java.util.Optional;
import java.util.function.Function;

public final class OptionalFocusableObservableComponent implements Component {
    public final OptionalFocusableObservable<Component> optional;
    public final Function<FocusableObservable<Component>, FocusableObservableComponent> liftComponent;

    public OptionalFocusableObservableComponent(
            OptionalFocusableObservable<Component> optional,
            Function<FocusableObservable<Component>, FocusableObservableComponent> liftComponent
    ) {
        this.optional = optional;
        this.liftComponent = liftComponent;
    }

    public static OptionalFocusableObservableComponent vertical(OptionalFocusableObservable<Component> optional) {
        return new OptionalFocusableObservableComponent(optional, FocusableObservableComponent::vertical);
    }

    public static OptionalFocusableObservableComponent horizontal(OptionalFocusableObservable<Component> optional) {
        return new OptionalFocusableObservableComponent(optional, FocusableObservableComponent::horizontal);
    }

    @Override
    public Maybe<OptionalFocusableObservableComponent> update(KeyStroke keyStroke) {
        return optional.optionalObservable
                .map(observable -> liftComponent.apply(observable).update(keyStroke)
                        .map(component -> new OptionalFocusableObservableComponent(new OptionalFocusableObservable<>(Optional.of(component.observable)), liftComponent)))
                .orElse(Maybe.empty());
    }

    @Override
    public Screen render(boolean selected) {
        return optional.optionalObservable
                .map(x -> liftComponent.apply(x).render(selected))
                .orElse(Screen.empty);
    }
}
