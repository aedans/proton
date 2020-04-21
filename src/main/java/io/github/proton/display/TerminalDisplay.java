/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.display;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import java.io.Closeable;
import java.io.IOException;

public final class TerminalDisplay implements Closeable {
    private TerminalScreen screen = new DefaultTerminalFactory()
            .setTerminalEmulatorTitle("Proton")
            .setPreferTerminalEmulator(false)
            .createScreen();

    public TerminalDisplay() throws IOException {
        screen.startScreen();
        screen.setCursorPosition(null);
    }

    public KeyStroke read() throws IOException {
        return screen.readInput();
    }

    public void write(TextCharacter character, TerminalPosition position) {
        screen.setCharacter(position.getColumn(), position.getRow(), character);
    }

    public void background(TextCharacter character) {
        for (int columns = 0; columns < screen.getTerminalSize().getColumns(); columns++) {
            for (int rows = 0; rows < screen.getTerminalSize().getRows(); rows++) {
                write(character, new TerminalPosition(columns, rows));
            }
        }
    }

    public void setCursor(TerminalPosition position) {
        screen.setCursorPosition(position);
    }

    public void resizeIfNecessary() {
        screen.doResizeIfNecessary();
    }

    public void refresh() throws IOException {
        screen.refresh();
    }

    public void clear() {
        screen.clear();
    }

    @Override
    public void close() throws IOException {
        screen.close();
    }
}
