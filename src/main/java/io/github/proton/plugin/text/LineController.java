package io.github.proton.plugin.text;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Component;
import io.github.proton.display.Controller;
import io.vavr.control.Option;
import org.pf4j.Extension;

@Extension
public final class LineController implements Controller.Of<LineComponent> {
    @Override
    public Class<LineComponent> clazz() {
        return LineComponent.class;
    }

    @Override
    public Option<Component> update(LineComponent component, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Character) {
            return Option.some(component.insert(component.getFocus().setCharacter(keyStroke.getCharacter())));
        } else {
            return Option.none();
        }
    }
}
