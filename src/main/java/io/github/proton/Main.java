package io.github.proton;

import io.github.proton.editor.*;
import io.github.proton.plugins.Plugins;
import io.github.proton.plugins.java.tree.*;
import io.vavr.collection.Vector;

import javax.swing.*;
import java.io.File;

public final class Main {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void main(String[] args) {
        var home = new File(args.length == 0 ? "." : args[0]);

        Plugins.start();

        var tree = new JavaFile(new JavaPackageDeclaration(new JavaIdentifier("")), Vector.of(), Vector.of());

        var editor = new Editor<>(
            Plugins.getExtensions(Style.class).get(0),
            Projector.get((Class) tree.getClass()),
            tree,
            new Position(0, 0),
            0);

        SwingUtilities.invokeLater(() -> new Display<>(editor));
    }
}
