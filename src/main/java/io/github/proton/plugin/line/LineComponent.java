package io.github.proton.plugin.line;

import io.github.proton.plugin.character.CharacterComponent;
import io.github.proton.plugin.list.EditableListComponent;
import io.vavr.control.Option;

public interface LineComponent extends EditableListComponent<CharacterComponent> {
    @Override
    LineComponent next();

    @Override
    LineComponent prev();

    @Override
    Option<? extends LineComponent> insert(CharacterComponent component);

    @Override
    Option<? extends LineComponent> delete();
}
