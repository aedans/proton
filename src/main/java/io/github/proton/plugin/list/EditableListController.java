package io.github.proton.plugin.list;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.Plugins;
import io.github.proton.display.Component;
import io.github.proton.display.Controller;
import io.vavr.control.Option;
import org.pf4j.Extension;

@SuppressWarnings({"rawtypes", "unchecked"})
@Extension
public final class EditableListController extends Controller.Of<EditableListComponent> {
    public EditableListController() {
        super(EditableListComponent.class);
    }

    @Override
    public Option<Component> update(EditableListComponent component, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Delete) {
            return component.delete();
        } else if (keyStroke.getKeyType() == KeyType.Backspace) {
            return component.prev().delete();
        } else {
            return Plugins.controller()
                    .updateGeneric((Component) component.getComponents().get(component.getIndex()), keyStroke)
                    .flatMap(c -> {
                        Option<EditableListComponent> delete = component.delete();
                        return delete.flatMap(x -> x.insert(c));
                    });
        }
    }
}
