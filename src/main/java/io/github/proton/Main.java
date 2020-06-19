/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton;

import io.github.proton.editor.*;
import io.github.proton.plugins.Plugins;
import io.github.proton.plugins.java.tree.JavaFile;
import io.github.proton.plugins.java.tree.JavaIdentifier;
import io.github.proton.plugins.java.tree.JavaPackageDeclaration;
import io.vavr.collection.Vector;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

public final class Main {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void main(String[] args) throws IOException {
        File home = new File(args.length == 0 ? "." : args[0]);

        Plugins.start();

        // Object tree = FileReader.instance.read(home).get();
        Object tree = new JavaFile(new JavaPackageDeclaration(new JavaIdentifier("")), Vector.of(), Vector.of());

        Editor<?> editor = new Editor<>(
                Plugins.getExtensions(Style.class).get(0), Projector.get((Class) tree.getClass()), tree, new Position(
                        0, 0));

        SwingUtilities.invokeLater(() -> new Display<>(editor));
    }
}
