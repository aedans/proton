package io.github.proton.plugin.text;

import io.github.proton.plugin.character.CharacterComponent;
import io.github.proton.plugin.character.InlineCharacterComponent;
import io.vavr.collection.Vector;

public final class InlineLineComponent implements LineComponent {
    public final Vector<CharacterComponent> start;
    public final Vector<CharacterComponent> end;
    public final CharacterComponent focus;

    public InlineLineComponent(Vector<CharacterComponent> start,
                               Vector<CharacterComponent> end,
                               CharacterComponent focus) {
        this.start = start;
        this.end = end;
        this.focus = focus;
    }

    public static InlineLineComponent of(Vector<CharacterComponent> characters) {
        characters = characters.append(new InlineCharacterComponent(' '));
        return new InlineLineComponent(Vector.empty(), characters.drop(1), characters.get());
    }

    @Override
    public Vector<CharacterComponent> getStart() {
        return start;
    }

    @Override
    public Vector<CharacterComponent> getEnd() {
        return end;
    }

    @Override
    public CharacterComponent getFocus() {
        return focus;
    }

    @Override
    public LineComponent setStart(Vector<CharacterComponent> start) {
        return new InlineLineComponent(start, end, focus);
    }

    @Override
    public LineComponent setEnd(Vector<CharacterComponent> end) {
        return new InlineLineComponent(start, end, focus);
    }

    @Override
    public LineComponent setFocus(CharacterComponent focus) {
        return new InlineLineComponent(start, end, focus);
    }
}
