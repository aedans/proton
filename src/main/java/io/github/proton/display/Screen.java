package io.github.proton.display;

import com.googlecode.lanterna.TextCharacter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public final class Screen {
    public final Observable<Observable<TextCharacter>> chars;
    public final Single<Long> width;
    public final Single<Long> height;

    public Screen(Observable<Observable<TextCharacter>> chars) {
        this(chars, chars.flatMapSingle(Observable::count).reduce(0L, (a, b) -> a > b ? a : b), chars.count());
    }

    public Screen(Observable<Observable<TextCharacter>> chars, Single<Long> width, Single<Long> height) {
        this.chars = chars;
        this.width = width;
        this.height = height;
    }

    public static TextCharacter inverse(TextCharacter character) {
        return character.withForegroundColor(character.getBackgroundColor()).withBackgroundColor(character.getForegroundColor());
    }

    public Screen inverse() {
        Observable<Observable<TextCharacter>> text = chars.map(x -> x.map(Screen::inverse));
        return new Screen(text, width, height);
    }
}
