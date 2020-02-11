package io.github.proton.plugin.line;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Component;
import io.github.proton.display.Controller;
import io.github.proton.plugin.character.CharacterComponent;
import io.vavr.control.Option;
import org.pf4j.Extension;

@Extension
public final class LineController extends Controller.Of<LineComponent> {
    public LineController() {
        super(LineComponent.class);
    }

    @Override
    public Option<? extends Component> update(LineComponent line, KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Character) {
            CharacterComponent character = line.getFocus().setCharacter(keyStroke.getCharacter());
            return line.insert(character).map(LineComponent::next);
        } else {
            return Option.none();
        }
    }
}
