package io.github.proton.plugin.text;

import io.github.proton.plugin.character.CharacterComponent;
import io.github.proton.plugin.list.EditableListComponent;
import io.github.proton.plugin.list.ListComponent;
import io.github.proton.plugin.list.NavigableListComponent;
import io.vavr.collection.Vector;
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
