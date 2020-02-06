package io.github.proton.plugin.text;

import io.github.proton.display.Screen;
import io.github.proton.display.Style;
import io.github.proton.plugin.character.CharacterComponent;
import io.github.proton.plugin.character.InlineCharacterComponent;
import io.github.proton.plugin.list.NavigableListComponent;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

public final class LiteralLineComponent implements LineComponent {
    public final Vector<CharacterComponent> components;
    public final CharacterComponent openQuote;
    public final CharacterComponent closeQuote;
    public final int index;

    public LiteralLineComponent(Vector<CharacterComponent> components, CharacterComponent openQuote, CharacterComponent closeQuote, int index) {
        this.components = components;
        this.openQuote = openQuote;
        this.closeQuote = closeQuote;
        this.index = index;
    }

    public static LiteralLineComponent of(Vector<CharacterComponent> characters) {
        return new LiteralLineComponent(characters, new InlineCharacterComponent('"'), new InlineCharacterComponent('"'), 0);
    }

    @Override
    public Vector<CharacterComponent> getComponents() {
        return components.append(closeQuote);
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
    public LiteralLineComponent next() {
        return new LiteralLineComponent(components, openQuote, closeQuote, NavigableListComponent.bounded(index + 1, getComponents()));
    }

    @Override
    public LiteralLineComponent prev() {
        return new LiteralLineComponent(components, openQuote, closeQuote, NavigableListComponent.bounded(index - 1, getComponents()));
    }

    @Override
    public Option<LiteralLineComponent> insert(CharacterComponent component) {
        if (index == components.size()) {
            return Option.some(new LiteralLineComponent(components.append(component), openQuote, closeQuote, index));
        } else if (NavigableListComponent.isBounded(index, components)) {
            return Option.some(new LiteralLineComponent(components.insert(index, component), openQuote, closeQuote, index));
        } else {
            return Option.none();
        }
    }

    @Override
    public Option<LiteralLineComponent> delete() {
        if (NavigableListComponent.isBounded(index, components)) {
            return Option.some(new LiteralLineComponent(components.removeAt(index), openQuote, closeQuote, index));
        } else {
            return Option.none();
        }
    }

    @Override
    public Screen render(Style style, boolean selected) {
        return NavigableListComponent.render(
                getComponents().prepend(openQuote),
                getOrientation(),
                index + 1,
                style.withBase("string"),
                selected
        );
    }

    @Override
    public String toString() {
        return "LiteralLineComponent{" +
                "components=" + components +
                ", index=" + index +
                '}';
    }
}
