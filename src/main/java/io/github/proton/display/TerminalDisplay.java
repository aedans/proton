package io.github.proton.display;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

import java.io.Closeable;
import java.io.IOException;

public final class TerminalDisplay implements Closeable {
    private final TerminalScreen screen = Single.fromSupplier(() -> new DefaultTerminalFactory()
            .setTerminalEmulatorTitle("Proton")
            .setPreferTerminalEmulator(false)
            .createScreen()).blockingGet();

    public TerminalDisplay() throws IOException {
        screen.startScreen();
        screen.setCursorPosition(null);
    }

    public Single<KeyStroke> readChar() {
        return Single.fromSupplier(screen::readInput);
    }

    public Completable writeChar(TextCharacter character, TerminalPosition position) {
        return Completable.fromAction(() -> screen.setCharacter(position.getColumn(), position.getRow(), character));
    }

    public Completable resizeIfNecessary() {
        return Completable.fromAction(screen::doResizeIfNecessary);
    }

    public Completable refresh() {
        return Completable.fromAction(screen::refresh);
    }

    public Completable clear() {
        return Completable.fromAction(screen::clear);
    }

    @Override
    public void close() throws IOException {
        screen.close();
    }
}
