package io.github.proton.plugin.text;

import io.github.proton.plugin.character.CharacterComponent;
import io.github.proton.plugin.list.ListComponent;
import io.vavr.collection.Vector;

public interface LineComponent extends ListComponent<CharacterComponent> {
    @Override
    LineComponent setComponents(Vector<CharacterComponent> components);

    @Override
    LineComponent setIndex(int index);
}
