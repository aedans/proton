package io.github.proton;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Component;
import io.github.proton.display.Screen;
import io.github.proton.display.TerminalDisplay;
import io.github.proton.plugins.directory.Directory;
import io.github.proton.plugins.json.JsonPlugin;

import java.io.File;

public final class Main {
    public static void main(String[] args) {
        new JsonPlugin().init();
        String home = args.length == 0 ? "." : args[0];
        try (TerminalDisplay display = new TerminalDisplay()) {
            Component component = new Directory(new File(home));
            while (true) {
                display.clear().blockingAwait();
                display.resizeIfNecessary().blockingAwait();
                Screen screen = component.render(true);
                display.writeCharss(new TerminalPosition(0, 0), screen.chars).blockingAwait();
                display.refresh().blockingAwait();
                KeyStroke keyStroke = display.read().blockingGet();
                if (keyStroke.getKeyType() == KeyType.EOF) break;
                Component component1 = component.update(keyStroke).blockingGet();
                if (component1 != null) component = component1;
            }
        }
    }
}
