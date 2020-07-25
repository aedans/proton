package io.github.proton.ui;

import io.github.proton.editor.*;
import io.github.proton.plugins.Plugins;
import io.github.proton.plugins.java.tree.*;
import io.vavr.collection.Vector;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

import static javax.swing.ScrollPaneConstants.*;

public final class Proton extends JFrame {
    public Proton(Path path) throws IOException {
        var editor = new EditorComponent(new Editor<>(new JavaFile(new JavaPackageDeclaration(new JavaIdentifier("")), Vector.of(), Vector.of())));
        var paths = new PathComponent(path, editor);
        var pane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            new JScrollPane(paths, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER),
            new JScrollPane(editor, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER));
        setContentPane(pane);
        pack();

        var size = Toolkit.getDefaultToolkit().getScreenSize();

        setTitle("Proton");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(size.width / 2, size.height / 2);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        Plugins.start();
        UIManager.setLookAndFeel(new ProtonLookAndFeel(Plugins.getTheme()));
        SwingUtilities.invokeLater(() -> {
            try {
                new Proton(Path.of(args.length == 0 ? "." : args[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
