package io.github.proton.plugin.project;

import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.github.proton.display.Style;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

import java.util.function.Function;

public interface CycleProjectionComponent<T extends Component> extends Component {
    T getComponent();

    Option<? extends CycleProjectionComponent<T>> updateComponents(Function<T, Option<T>> function);

    CycleProjectionComponent<T> next();

    final class Impl<T extends Component> implements CycleProjectionComponent<T> {
        public final Vector<T> components;

        public Impl(Vector<T> components) {
            this.components = components;
        }

        @Override
        public T getComponent() {
            return components.get();
        }

        @Override
        public Option<CycleProjectionComponent.Impl<T>> updateComponents(Function<T, Option<T>> function) {
            Option<Vector<T>> vectors = components
                    .map(function)
                    .foldRight(Option.some(Vector.empty()), (o, v) -> v.flatMap(vector -> o.map(vector::prepend)));
            return vectors.map(Impl::new);
        }

        @Override
        public CycleProjectionComponent.Impl<T> next() {
            return new Impl<>(components.drop(1).append(components.get()));
        }
    }

    @Override
    default Screen render(Style style, boolean selected) {
        return getComponent().render(style, selected);
    }
}
