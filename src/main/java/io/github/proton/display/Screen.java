package io.github.proton.display;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;

import java.util.function.UnaryOperator;

public final class Screen {
    public final Seq<Tuple2<TextCharacter, TerminalPosition>> characters;
    public final int rows;
    public final int columns;

    public Screen(Seq<Tuple2<TextCharacter, TerminalPosition>> characters,
                  int rows,
                  int columns) {
        this.characters = characters;
        this.rows = rows;
        this.columns = columns;
    }

    public Screen(Seq<Tuple2<TextCharacter, TerminalPosition>> characters) {
        this(
                characters,
                characters.map(x -> x._2.getRow() + 1).fold(0, Math::max),
                characters.map(x -> x._2.getColumn() + 1).fold(0, Math::max)
        );
    }

    public static final Screen empty = new Screen(List.empty());

    public static Screen of(TextCharacter... characters) {
        return of(Vector.of(characters));
    }

    public static Screen of(Seq<TextCharacter> characters) {
        return new Screen(characters.zipWithIndex(((character, integer) ->
                new Tuple2<>(character, new TerminalPosition(integer, 0)))));
    }

    public static TextCharacter invert(TextCharacter character) {
        return character
                .withForegroundColor(character.getBackgroundColor())
                .withBackgroundColor(character.getForegroundColor());
    }

    public Screen map(UnaryOperator<TextCharacter> mapping) {
        return new Screen(characters.map(t -> t.map1(mapping)));
    }

    public Screen horizontalPlus(Screen screen) {
        return new Screen(characters.appendAll(screen.characters.map(t -> t.map2(p -> p.withRelativeColumn(columns)))));
    }

    public Screen verticalPlus(Screen screen) {
        return new Screen(characters.appendAll(screen.characters.map(t -> t.map2(p -> p.withRelativeRow(rows)))));
    }

    public Screen invert() {
        return map(Screen::invert);
    }
}
