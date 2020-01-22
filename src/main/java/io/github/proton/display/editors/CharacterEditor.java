package io.github.proton.display.editors;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Editor;
import io.github.proton.display.Screen;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public final class CharacterEditor implements Editor<Character> {
    private final char character;

    private CharacterEditor(char character) {
        this.character = character;
    }

    public static Editor<Character> of(char character) {
        return new CharacterEditor(character);
    }

    @Override
    public Maybe<Editor<Character>> update(KeyStroke keyStroke) {
        return Maybe.empty();
    }

    @Override
    public Single<Screen> render(boolean selected) {
        Screen screen = Screen.of(new TextCharacter(character, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        return Single.just(selected ? screen.invert() : screen);
    }

    @Override
    public Character get() {
        return character;
    }
}
