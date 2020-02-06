package io.github.proton.display;

import com.googlecode.lanterna.input.KeyStroke;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import org.pf4j.ExtensionPoint;

public interface Controller extends ExtensionPoint {
    Option<Component> updateGeneric(Component component, KeyStroke keyStroke);

    int precedence();

    default Controller combine(Controller controller) {
        return new Controller() {
            @Override
            public Option<Component> updateGeneric(Component component, KeyStroke keyStroke) {
                return Controller.this.updateGeneric(component, keyStroke)
                        .orElse(controller.updateGeneric(component, keyStroke));
            }

            @Override
            public int precedence() {
                return Math.min(Controller.this.precedence(), controller.precedence());
            }
        };
    }

    Controller unit = new Controller() {
        @Override
        public Option<Component> updateGeneric(Component component, KeyStroke keyStroke) {
            return Option.none();
        }

        @Override
        public int precedence() {
            return 0;
        }
    };

    @SuppressWarnings("unchecked")
    abstract class Of<T extends Component> implements Controller {
        private final Class<T> clazz;

        protected Of(Class<T> clazz) {
            this.clazz = clazz;
        }

        public abstract Option<? extends Component> update(T component, KeyStroke keyStroke);

        @Override
        public int precedence() {
            return precedence(clazz);
        }

        public static <T> int precedence(Class<T> clazz) {
            return Vector.of(clazz.getInterfaces())
                    .prependAll(Option.of(clazz.getSuperclass()).toVector())
                    .map(x -> precedence(x) + 1)
                    .maxBy(Integer::compareTo)
                    .getOrElse(0);
        }

        @Override
        public Option<Component> updateGeneric(Component component, KeyStroke keyStroke) {
            if (clazz.isInstance(component)) {
                return (Option<Component>) update(clazz.cast(component), keyStroke);
            } else {
                return Option.none();
            }
        }
    }
}
