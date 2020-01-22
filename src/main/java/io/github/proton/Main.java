package io.github.proton;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Editor;
import io.github.proton.display.Screen;
import io.github.proton.display.TerminalDisplay;
import io.github.proton.display.editors.CharacterEditor;
import io.github.proton.display.editors.ListEditor;
import io.github.proton.txt.Line;

import java.io.IOException;

public final class Main {
    public static void main(String[] args) throws IOException {
        String home = args.length == 0 ? "." : args[0];
        try (TerminalDisplay display = new TerminalDisplay()) {
            Line line = Line.of("Hello, world!");
            Editor<?> editor = ListEditor.of(
                    Screen::horizontalPlus,
                    line.chars.map(CharacterEditor::of),
                    Line.listView,
                    line).blockingGet();
            while (true) {
                display.clear().blockingAwait();
                display.resizeIfNecessary().blockingAwait();
                Screen screen = editor.render(true).blockingGet();
                screen.characters.zipWith(screen.positions, display::write).flatMapCompletable(x -> x).blockingAwait();
                display.refresh().blockingAwait();
                KeyStroke keyStroke = display.read().blockingGet();
                if (keyStroke.getKeyType() == KeyType.EOF) break;
                Editor<?> editor1 = editor.update(keyStroke).blockingGet();
                if (editor1 != null) editor = editor1;
            }
        }
    }
}
