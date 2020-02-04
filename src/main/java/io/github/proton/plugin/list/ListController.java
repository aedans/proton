package io.github.proton.plugin.list;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.Plugins;
import io.github.proton.display.Component;
import io.github.proton.display.Controller;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import org.pf4j.Extension;

@SuppressWarnings({"rawtypes", "unchecked"})
@Extension
public final class ListController extends Controller.Of<ListComponent> {
    public ListController() {
        super(ListComponent.class);
    }

    @Override
    public Option<Component> update(ListComponent component, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            return Option.some(component.setIndex(bounded(component.getIndex() + 1, component.getComponents())));
        } else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            return Option.some(component.setIndex(bounded(component.getIndex() - 1, component.getComponents())));
        } else if (keyStroke.getKeyType() == KeyType.Delete) {
            if (component.getIndex() >= component.getComponents().size() - 1) {
                return Option.none();
            } else {
                return Option.some(component.setComponents(component.getComponents().removeAt(component.getIndex())));
            }
        } else if (keyStroke.getKeyType() == KeyType.Backspace) {
            if (component.getIndex() == 0) {
                return Option.none();
            } else {
                return Option.some(component
                        .setComponents(component.getComponents().removeAt(component.getIndex() - 1))
                        .setIndex(bounded(component.getIndex() - 1, component.getComponents())));
            }
        } else {
            return Plugins.controller()
                    .updateGeneric((Component) component.getComponents().get(component.getIndex()), keyStroke)
                    .map(c -> component.setComponents(component.getComponents().update(component.getIndex(), c)));
        }
    }

    public static <T> int bounded(int index, Vector<T> vector) {
        if (index < 0) {
            return 0;
        } else if (index >= vector.size()) {
            return vector.size() - 1;
        } else {
            return index;
        }
    }
}
