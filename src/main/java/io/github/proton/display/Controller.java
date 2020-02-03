package io.github.proton.display;

import com.googlecode.lanterna.input.KeyStroke;
import io.vavr.control.Option;
import org.pf4j.ExtensionPoint;

public interface Controller extends ExtensionPoint {
    Option<Component> updateGeneric(Component component, KeyStroke keyStroke);

    default Controller combine(Controller controller) {
        return (component, keyStroke) -> Controller.this.updateGeneric(component, keyStroke)
                .orElse(controller.updateGeneric(component, keyStroke));
    }

    Controller unit = (component, keyStroke) -> Option.none();

    abstract class Of<T extends Component> implements Controller {
        private final Class<T> clazz;

        public Of(Class<T> clazz) {
            this.clazz = clazz;
        }

        public abstract Option<Component> update(T component, KeyStroke keyStroke);

        @Override
        public Option<Component> updateGeneric(Component component, KeyStroke keyStroke) {
            if (clazz.isInstance(component)) {
                return update(clazz.cast(component), keyStroke);
            } else {
                return Option.none();
            }
        }
    }
}
