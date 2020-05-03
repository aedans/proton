/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import io.github.proton.display.Editor;
import io.github.proton.display.Projector;
import io.github.proton.display.Style;
import io.github.proton.display.TerminalDisplay;
import io.github.proton.plugins.Plugins;
import io.github.proton.plugins.java.tree.JavaFile;
import io.github.proton.plugins.java.tree.JavaIdentifier;
import io.github.proton.plugins.java.tree.JavaPackageDeclaration;
import io.vavr.collection.Vector;
import java.io.File;
import java.io.IOException;

public final class Main {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void main(String[] args) throws IOException {
        File home = new File(args.length == 0 ? "." : args[0]);

        Plugins.start();

        // Object tree = FileReader.instance.read(home).get();
        Object tree = new JavaFile(new JavaPackageDeclaration(new JavaIdentifier("")), Vector.of(), Vector.of());

        Editor<?> editor = new Editor<>(
                Plugins.getExtensions(Style.class).get(0),
                Projector.get((Class) tree.getClass()),
                tree,
                TerminalPosition.TOP_LEFT_CORNER);

        try (TerminalDisplay display = new TerminalDisplay()) {
            while (true) {
                display.clear();
                display.resizeIfNecessary();
                editor.render(display);
                display.refresh();
                KeyStroke keyStroke = display.read();
                if (keyStroke.getKeyType() == KeyType.EOF) break;
                editor = editor.update(keyStroke);
            }
        }
    }
}
