package io.github.proton.display.components;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public final class CharacterComponent implements Component {
    private final TextCharacter character;

    private CharacterComponent(TextCharacter character) {
        this.character = character;
    }

    public static CharacterComponent of(char character) {
        return new CharacterComponent(new TextCharacter(character));
    }

    public static CharacterComponent of(TextCharacter character) {
        return new CharacterComponent(character);
    }

    @Override
    public Maybe<Component> update(KeyStroke keyStroke) {
        return Maybe.empty();
    }

    @Override
    public Single<Screen> render(boolean selected) {
        return Single.just(Screen.of(character));
    }
}
