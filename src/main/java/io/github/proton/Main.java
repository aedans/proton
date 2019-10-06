package io.github.proton;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.input.KeyStroke;
import io.github.proton.display.TerminalDisplay;
import io.github.proton.json.JsonComponent;

public final class Main {
    public static void main(String[] args) throws Throwable {
        try (TerminalDisplay display = new TerminalDisplay()) {
            while (true) {
                KeyStroke keyStroke = display.read().blockingGet();
                display.writeCharss(new TerminalPosition(0, 0), new JsonComponent(keyStroke).render()).blockingAwait();
                display.refresh().blockingAwait();
                display.clear().blockingAwait();
            }
        }
    }
}
