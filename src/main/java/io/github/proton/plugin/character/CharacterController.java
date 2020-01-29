package io.github.proton.plugin.character;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Component;
import io.github.proton.display.Controller;
import io.vavr.control.Option;
import org.pf4j.Extension;

@Extension
public final class CharacterController implements Controller.Of<CharacterComponent> {
    @Override
    public Class<CharacterComponent> clazz() {
        return CharacterComponent.class;
    }

    @Override
    public Option<Component> update(CharacterComponent component, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Character) {
            return Option.some(component.setCharacter(keyStroke.getCharacter()));
        } else {
            return Option.none();
        }
    }
}
