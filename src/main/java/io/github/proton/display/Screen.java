package io.github.proton.display;

import com.googlecode.lanterna.TextCharacter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public final class Screen {
    public static final Screen empty = new Screen(Observable.empty());
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

    public static Observable<Observable<TextCharacter>> extendWidth(Observable<Observable<TextCharacter>> chars, long width) {
        return chars.map(row ->
                Observable.concat(row, Observable.fromSupplier(() -> new TextCharacter(' ')))
                        .take(width));
    }

    public static Observable<Observable<TextCharacter>> extendHeight(Observable<Observable<TextCharacter>> chars, long height) {
        return Observable.concat(chars, Observable.fromSupplier(Observable::<TextCharacter>empty)).take(height);
    }

    public Screen inverse() {
        Observable<Observable<TextCharacter>> text = chars.map(x -> x.map(Screen::inverse));
        return new Screen(text, width, height);
    }

    public Screen verticalPlus(Screen screen) {
        Single<Long> width = this.width.flatMap(width1 -> screen.width.map(width2 -> width1 > width2 ? width1 : width2));
        Single<Long> height = this.height.flatMap(height1 -> screen.height.map(height2 -> height1 + height2));
        Observable<Observable<TextCharacter>> chars1 = width.flatMapObservable(w -> extendWidth(chars, w));
        Observable<Observable<TextCharacter>> chars2 = width.flatMapObservable(w -> extendWidth(screen.chars, w));
        Observable<Observable<TextCharacter>> textChars = Observable.concat(chars1, chars2);
        return new Screen(textChars, width, height);
    }

    public Screen horizontalPlus(Screen screen) {
        Single<Long> width = this.width.flatMap(width1 -> screen.width.map(width2 -> width1 + width2));
        Single<Long> height = this.height.flatMap(height1 -> screen.height.map(height2 -> height1 > height2 ? height1 : height2));
        Observable<Observable<TextCharacter>> chars1 = height.flatMapObservable(h -> extendHeight(chars, h));
        Observable<Observable<TextCharacter>> chars2 = height.flatMapObservable(h -> extendHeight(screen.chars, h));
        Observable<Observable<TextCharacter>> textChars = chars1.zipWith(chars2, Observable::concat);
        return new Screen(textChars, width, height);
    }
}
