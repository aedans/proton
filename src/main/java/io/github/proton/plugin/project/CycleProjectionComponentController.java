package io.github.proton.plugin.project;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.Plugins;
import io.github.proton.display.Component;
import io.github.proton.display.Controller;
import io.vavr.control.Option;
import org.pf4j.Extension;

@SuppressWarnings({"rawtypes", "unchecked"})
@Extension
public final class CycleProjectionComponentController extends Controller.Of<CycleProjectionComponent> {
    public CycleProjectionComponentController() {
        super(CycleProjectionComponent.class);
    }

    @Override
    public Option<Component> update(CycleProjectionComponent component, KeyStroke keyStroke) {
        return component.updateComponent(c -> Plugins.controller().updateGeneric((Component) c, keyStroke)
                .orElse(() -> {
                    if (keyStroke.getKeyType() == KeyType.Tab) {
                        return Option.some(component.next());
                    } else {
                        return Option.none();
                    }
                }));
    }
}
