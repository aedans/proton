package io.github.proton.display;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.io.Closeable;

public final class TerminalDisplay implements Closeable {
    private final TerminalScreen screen = Single.fromSupplier(() -> new DefaultTerminalFactory()
            .setTerminalEmulatorTitle("Proton")
            .setPreferTerminalEmulator(false)
            .createScreen()).blockingGet();

    public TerminalDisplay() {
        start().blockingAwait();
        screen.setCursorPosition(null);
    }

    public Single<KeyStroke> read() {
        return Single.fromSupplier(screen::readInput);
    }

    public Completable writeChar(TerminalPosition position, TextCharacter character) {
        return Completable.fromAction(() -> screen.setCharacter(position.getColumn(), position.getRow(), character));
    }

    public Completable writeChars(TerminalPosition position, Observable<TextCharacter> characters) {
        return characters.count()
                .flatMapObservable(count -> Observable.range(0, count.intValue()))
                .map(position::withRelativeColumn)
                .zipWith(characters, this::writeChar)
                .concatMapCompletable(x -> x);
    }

    public Completable writeCharss(TerminalPosition position, Observable<Observable<TextCharacter>> characterss) {
        return characterss.count()
                .flatMapObservable(count -> Observable.range(0, count.intValue()))
                .map(position::withRelativeRow)
                .zipWith(characterss, this::writeChars)
                .concatMapCompletable(x -> x);
    }

    public Completable refresh() {
        return Completable.fromAction(screen::refresh);
    }

    public Completable clear() {
        return Completable.fromAction(screen::clear);
    }

    public Completable start() {
        return Completable.fromAction(screen::startScreen).andThen(refresh());
    }

    public Completable stop() {
        return Completable.fromAction(screen::stopScreen).andThen(refresh());
    }

    @Override
    public void close() {
        stop().blockingAwait();
    }
}
