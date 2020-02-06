package io.github.proton.plugin.list;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Component;
import io.github.proton.display.Controller;
import io.vavr.control.Option;
import org.pf4j.Extension;

@SuppressWarnings({"rawtypes"})
@Extension
public final class NavigableListController extends Controller.Of<NavigableListComponent> {
    public NavigableListController() {
        super(NavigableListComponent.class);
    }

    @Override
    public Option<Component> update(NavigableListComponent component, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            return Option.some(component.next());
        } else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            return Option.some(component.prev());
        } else {
            return Option.none();
        }
    }
}
