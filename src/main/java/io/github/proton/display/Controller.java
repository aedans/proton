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

    interface Of<T extends Component> extends Controller {
        Class<T> clazz();

        Option<Component> update(T component, KeyStroke keyStroke);

        @Override
        default Option<Component> updateGeneric(Component component, KeyStroke keyStroke) {
            if (clazz().isInstance(component)) {
                return update(clazz().cast(component), keyStroke);
            } else {
                return Option.none();
            }
        }
    }
}
