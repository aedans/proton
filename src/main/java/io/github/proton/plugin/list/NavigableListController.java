package io.github.proton.plugin.list;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Component;
import io.github.proton.display.Controller;
import io.vavr.control.Option;
import org.pf4j.Extension;

import static io.github.proton.plugin.list.ListComponent.Orientation.*;

@SuppressWarnings({"rawtypes"})
@Extension
public final class NavigableListController extends Controller.Of<NavigableListComponent> {
    public NavigableListController() {
        super(NavigableListComponent.class);
    }

    @Override
    public Option<Component> update(NavigableListComponent component, KeyStroke keyStroke) {
        if (component.getOrientation() == HORIZONTAL && keyStroke.getKeyType() == KeyType.ArrowRight) {
            return Option.some(component.next());
        } else if (component.getOrientation() == VERTICAL && keyStroke.getKeyType() == KeyType.ArrowDown) {
            return Option.some(component.next());
        } else if (component.getOrientation() == HORIZONTAL && keyStroke.getKeyType() == KeyType.ArrowLeft) {
            return Option.some(component.prev());
        } else if (component.getOrientation() == VERTICAL && keyStroke.getKeyType() == KeyType.ArrowUp) {
            return Option.some(component.prev());
        } else {
            return Option.none();
        }
    }
}
