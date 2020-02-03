package io.github.proton.plugin.character;

import io.github.proton.plugin.project.CycleProjectionComponent;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

import java.util.function.Function;

public final class CycleProjectionCharacterComponent implements CycleProjectionComponent<CharacterComponent>, CharacterComponent {
    public final Vector<CharacterComponent> components;

    public CycleProjectionCharacterComponent(Vector<CharacterComponent> components) {
        this.components = components;
    }

    @Override
    public char getCharacter() {
        return components.get().getCharacter();
    }

    @Override
    public CharacterComponent setCharacter(char character) {
        return new CycleProjectionCharacterComponent(components.map(x -> x.setCharacter(character)));
    }

    @Override
    public CharacterComponent getComponent() {
        return components.get();
    }

    @Override
    public Option<CycleProjectionCharacterComponent> updateComponent(Function<CharacterComponent, Option<CharacterComponent>> function) {
        return new Impl<>(components).updateComponent(function).map(x -> new CycleProjectionCharacterComponent(x.components));
    }

    @Override
    public CycleProjectionCharacterComponent next() {
        return new CycleProjectionCharacterComponent(new Impl<>(components).next().components);
    }
}
