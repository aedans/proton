package io.github.proton.display;

import com.googlecode.lanterna.TextCharacter;
import io.github.proton.util.ObservableUtil;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;

public final class Screen {
    public static final Screen empty = new Screen(Observable.empty());
    public final Observable<Observable<TextCharacter>> chars;
    public final long width;
    public final long height;

    public Screen(Observable<Observable<TextCharacter>> chars) {
        this(
                chars,
                chars.flatMapSingle(Observable::count).reduce(0L, (a, b) -> a > b ? a : b).blockingGet(),
                chars.count().blockingGet()
        );
    }

    public Screen(Observable<Observable<TextCharacter>> chars, long width, long height) {
        this.chars = chars;
        this.width = width;
        this.height = height;
    }

    public static Screen from(String string, Function<Character, TextCharacter> function) {
        return from(ObservableUtil.fromString(string).map(function));
    }

    public static Screen from(Observable<TextCharacter> line) {
        return new Screen(Observable.just(line));
    }

    public static TextCharacter inverse(TextCharacter character) {
        return character.withForegroundColor(character.getBackgroundColor()).withBackgroundColor(character.getForegroundColor());
    }

    public Screen extend(long width, long height) {
        Observable<Observable<TextCharacter>> lines = chars.count()
                .flatMapObservable(count -> Observable.range(0, (int) Math.max(0, height - count)))
                .map(x -> Observable.empty());
        Observable<Observable<TextCharacter>> chars = Observable.concat(this.chars, lines)
                .map(row -> {
                    Observable<TextCharacter> rest = row.count()
                            .flatMapObservable(count -> Observable.range(0, (int) Math.max(0, width - count)))
                            .map(x -> TextCharacter.DEFAULT_CHARACTER);
                    return Observable.concat(row, rest);
                });
        return new Screen(chars);
    }

    public Screen inverse() {
        Observable<Observable<TextCharacter>> text = chars.map(x -> x.map(Screen::inverse));
        return new Screen(text);
    }

    public Screen verticalPlus(Screen screen) {
        long width = Math.max(this.width, screen.width);
        Screen screen1 = this.extend(width, this.height);
        Screen screen2 = screen.extend(width, screen.height);
        Observable<Observable<TextCharacter>> textChars = Observable.concat(screen1.chars, screen2.chars);
        return new Screen(textChars);
    }

    public Screen horizontalPlus(Screen screen) {
        long height = Math.max(this.height, screen.height);
        Screen screen1 = this.extend(this.width, height);
        Screen screen2 = screen.extend(screen.width, height);
        Observable<Observable<TextCharacter>> textChars = screen1.chars.zipWith(screen2.chars, Observable::concat);
        return new Screen(textChars);
    }

    public Screen horizontalPlusLeft(Screen screen) {
        long height = Math.max(this.height, screen.height);
        Screen screen1 = this.extend(0, height);
        Screen screen2 = screen.extend(screen.width, height);
        Observable<Observable<TextCharacter>> textChars = screen1.chars.zipWith(screen2.chars, Observable::concat);
        return new Screen(textChars);
    }

    public Screen indent(int x) {
        Observable<TextCharacter> indent = Observable.range(0, x).map(i -> TextCharacter.DEFAULT_CHARACTER);
        return new Screen(chars.map(line -> Observable.concat(indent, line).cache()).cache());
    }
}
