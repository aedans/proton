package io.github.proton;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Renderer;
import io.github.proton.display.TerminalDisplay;
import io.github.proton.display.Updater;
import io.github.proton.plugins.file.VirtualDirectory;

import java.io.File;

public final class Main {
    public static void main(String[] args) throws Throwable {
        String home = args.length == 0 ? "." : args[0];
        try (TerminalDisplay display = new TerminalDisplay()) {
            Object component = new VirtualDirectory(new File(home));
            while (true) {
                display.clear().blockingAwait();
                Renderer.Render render = Renderer.renderer.render(component, TerminalPosition.TOP_LEFT_CORNER);
                display.writeCharss(new TerminalPosition(0, 0), render.screen).blockingAwait();
                display.setCursor(render.cursor.blockingGet()).blockingAwait();
                display.refresh().blockingAwait();
                KeyStroke keyStroke = display.read().blockingGet();
                if (keyStroke.getKeyType() == KeyType.EOF) break;
                component = Updater.updater.update(component, keyStroke);
            }
        }
    }
}
