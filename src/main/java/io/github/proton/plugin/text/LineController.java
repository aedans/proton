package io.github.proton.plugin.text;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Component;
import io.github.proton.display.Controller;
import io.github.proton.plugin.character.CharacterComponent;
import io.github.proton.plugin.character.InlineCharacterComponent;
import io.github.proton.plugin.list.ListController;
import io.vavr.control.Option;
import org.pf4j.Extension;

@Extension
public final class LineController extends Controller.Of<LineComponent> {
    public LineController() {
        super(LineComponent.class);
    }

    @Override
    public Option<Component> update(LineComponent line, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Character) {
            CharacterComponent character = line.getComponents().get(line.getIndex()).setCharacter(keyStroke.getCharacter());
            return Option.some(line
                    .setComponents(line.getComponents().insert(line.getIndex(), character))
                    .setIndex(line.getIndex() + 1));
        } else {
            return Option.none();
        }
    }
}
