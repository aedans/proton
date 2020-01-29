package io.github.proton.plugin.text;

import io.github.proton.plugin.character.CharacterComponent;
import io.github.proton.plugin.list.ListComponent;
import io.vavr.collection.Vector;

public interface LineComponent extends ListComponent<CharacterComponent> {
    @Override
    LineComponent setStart(Vector<CharacterComponent> start);

    @Override
    LineComponent setEnd(Vector<CharacterComponent> end);

    @Override
    LineComponent setFocus(CharacterComponent focus);
}
