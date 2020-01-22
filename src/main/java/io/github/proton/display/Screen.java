package io.github.proton.display;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public final class Screen {
    public final Observable<TextCharacter> characters;
    public final Observable<TerminalPosition> positions;
    public final Single<Integer> rows;
    public final Single<Integer> columns;

    public Screen(Observable<TextCharacter> characters,
                  Observable<TerminalPosition> positions,
                  Single<Integer> rows,
                  Single<Integer> columns) {
        this.characters = characters;
        this.positions = positions;
        this.rows = rows;
        this.columns = columns;
    }

    public static final Screen empty = of(Observable.empty(), Observable.empty());

    public static Screen of(TextCharacter character) {
        return of(Observable.just(character), Observable.just(TerminalPosition.TOP_LEFT_CORNER));
    }

    public static Screen of(Observable<TextCharacter> characters, Observable<TerminalPosition> positions) {
        return new Screen(
                characters,
                positions,
                positions.map(x -> x.getRow() + 1).reduce(0, Math::max),
                positions.map(x -> x.getColumn() + 1).reduce(0, Math::max)
        );
    }

    public static TextCharacter invert(TextCharacter character) {
        return character.withForegroundColor(character.getBackgroundColor()).withBackgroundColor(character.getForegroundColor());
    }

    public Screen horizontalPlus(Screen screen) {
        return of(
                characters.concatWith(screen.characters),
                columns.flatMapObservable(columns ->
                        positions.concatWith(screen.positions.map(x ->
                                x.withRelativeColumn(columns)))));
    }

    public Screen invert() {
        return of(characters.map(Screen::invert), positions);
    }
}
