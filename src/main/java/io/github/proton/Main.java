package io.github.proton;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Component;
import io.github.proton.display.TerminalDisplay;
import io.github.proton.plugins.directory.DirectoryComponent;

import java.io.File;

public final class Main {
    public static void main(String[] args) throws Throwable {
        String home = args.length == 0 ? "." : args[0];
        try (TerminalDisplay display = new TerminalDisplay()) {
            Component component = new DirectoryComponent(new File(home));
            while (true) {
                display.clear().blockingAwait();
                display.writeCharss(new TerminalPosition(0, 0), component.render()).blockingAwait();
                display.refresh().blockingAwait();
                KeyStroke keyStroke = display.read().blockingGet();
                if (keyStroke.getKeyType() == KeyType.EOF) break;
                component = component.update(keyStroke);
            }
        }
    }
}
