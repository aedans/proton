package io.github.proton.plugin.line;

import io.github.proton.Plugins;
import io.github.proton.plugin.character.CharacterComponent;
import io.github.proton.plugin.character.InlineCharacterComponent;
import io.github.proton.plugin.list.NavigableListComponent;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

public final class InlineLineComponent implements LineComponent {
    public final Vector<CharacterComponent> components;
    public final CharacterComponent trailingSpace;
    public final int index;

    public InlineLineComponent(Vector<CharacterComponent> components, CharacterComponent trailingSpace, int index) {
        this.components = components;
        this.trailingSpace = trailingSpace;
        this.index = NavigableListComponent.bounded(index, getComponents());
    }

    public static InlineLineComponent of(Vector<CharacterComponent> characters) {
        return new InlineLineComponent(characters, new InlineCharacterComponent(' '), 0);
    }

    @Override
    public Vector<CharacterComponent> getComponents() {
        return components.append(trailingSpace);
    }

    @Override
    public Orientation getOrientation() {
        return Orientation.HORIZONTAL;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public InlineLineComponent next() {
        return new InlineLineComponent(components, trailingSpace, index + 1);
    }

    @Override
    public InlineLineComponent prev() {
        return new InlineLineComponent(components, trailingSpace, index - 1);
    }

    @Override
    public Option<InlineLineComponent> insert(CharacterComponent component) {
        if (index == components.size()) {
            return Option.some(new InlineLineComponent(components.append(component), trailingSpace, index));
        } else if (NavigableListComponent.isBounded(index, components)) {
            return Option.some(new InlineLineComponent(components.insert(index, component), trailingSpace, index));
        } else {
            return Option.none();
        }
    }

    @Override
    public Option<InlineLineComponent> delete() {
        if (NavigableListComponent.isBounded(index, components)) {
            return Option.some(new InlineLineComponent(components.removeAt(index), trailingSpace, index));
        } else {
            return Option.none();
        }
    }

    @Override
    public String toString() {
        return "InlineLineComponent{" +
                "components=" + components +
                ", index=" + index +
                '}';
    }
}
