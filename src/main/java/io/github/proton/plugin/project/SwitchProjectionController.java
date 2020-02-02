package io.github.proton.plugin.project;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.Plugins;
import io.github.proton.display.Component;
import io.github.proton.display.Controller;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import org.pf4j.Extension;

@Extension
public final class SwitchProjectionController implements Controller.Of<SwitchProjectionComponent> {
    @Override
    public Class<SwitchProjectionComponent> clazz() {
        return SwitchProjectionComponent.class;
    }

    @Override
    public Option<Component> update(SwitchProjectionComponent component, KeyStroke keyStroke) {
        Controller controller = Plugins.controller();
        Option<Vector<Component>> components = component.getProjections()
                .map(c -> controller.updateGeneric(c, keyStroke))
                .foldRight(Option.some(Vector.empty()), (o, v) ->
                        v.flatMap(vector -> o.map(vector::prepend)));
        Option<Component> map = components.map(component::setProjections);
        return map.orElse(() -> {
            if (keyStroke.getKeyType() == KeyType.Tab) {
                if (component.getIndex() >= component.getProjections().size() - 1) {
                    return Option.some(component.setIndex(0));
                } else {
                    return Option.some(component.setIndex(component.getIndex() + 1));
                }
            } else {
                return Option.none();
            }
        });
    }
}
