package io.github.proton.plugin.list;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Component;
import io.github.proton.display.Controller;
import io.vavr.control.Option;
import org.pf4j.Extension;

@SuppressWarnings({"rawtypes", "unchecked"})
@Extension
public final class ListController implements Controller.Of<ListComponent> {
    @Override
    public Class<ListComponent> clazz() {
        return ListComponent.class;
    }

    @Override
    public Option<Component> update(ListComponent component, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.ArrowRight) {
            if (component.getEnd().isEmpty()) {
                return Option.none();
            } else {
                return Option.of(component
                        .setStart(component.getStart().append(component.getFocus()))
                        .setEnd(component.getEnd().drop(1))
                        .setFocus((Component) component.getEnd().get()));
            }
        } else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
            if (component.getStart().isEmpty()) {
                return Option.none();
            } else {
                return Option.of(component
                        .setEnd(component.getEnd().prepend(component.getFocus()))
                        .setStart(component.getStart().dropRight(1))
                        .setFocus((Component) component.getStart().last()));
            }
        } else if (keyStroke.getKeyType() == KeyType.Backspace) {
            if (component.getStart().isEmpty()) {
                return Option.none();
            } else {
                return Option.of(component
                        .setStart(component.getStart().dropRight(1)));
            }
        } else if (keyStroke.getKeyType() == KeyType.Delete) {
            if (component.getEnd().isEmpty()) {
                return Option.none();
            } else {
                return Option.of(component
                        .setEnd(component.getEnd().drop(1))
                        .setFocus((Component) component.getEnd().get()));
            }
        } else {
            return Option.none();
        }
    }
}
