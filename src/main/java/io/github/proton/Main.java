package io.github.proton;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.github.proton.display.TerminalDisplay;
import io.github.proton.display.components.ListComponent;

import java.io.IOException;

public final class Main {
    public static void main(String[] args) throws IOException {
        String home = args.length == 0 ? "." : args[0];
        try (TerminalDisplay display = new TerminalDisplay()) {
            Component component = ListComponent.of("Hello, world!");
            while (true) {
                display.clear().blockingAwait();
                display.resizeIfNecessary().blockingAwait();
                Screen screen = component.render(true).blockingGet();
                screen.characters.zipWith(screen.positions, display::writeChar).flatMapCompletable(x -> x).blockingAwait();
                display.refresh().blockingAwait();
                KeyStroke keyStroke = display.readChar().blockingGet();
                if (keyStroke.getKeyType() == KeyType.EOF) break;
                Component component1 = component.update(keyStroke).blockingGet();
                if (component1 != null) component = component1;
            }
        }
    }
}
