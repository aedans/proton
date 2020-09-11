package io.github.proton.ui;

import io.github.proton.editor.*;
import io.github.proton.io.PathReader;
import io.github.proton.plugins.Plugins;
import io.vavr.collection.Vector;

import javax.swing.*;
import javax.swing.tree.*;
import java.io.IOException;
import java.nio.file.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class PathComponent extends JTree {
    public PathComponent(Path path, EditorComponent editor) throws IOException {
        super(load(path));

        addTreeSelectionListener(e -> {
            var p = Path.of(Vector.range(0, e.getPath().getPathCount())
                .map(i -> e.getPath().getPathComponent(i).toString())
                .mkString("", "/", ""));
            try {
                if (!Files.isDirectory(p)) {
                    @SuppressWarnings("unchecked")
                    var tree = Plugins.getExtension(PathReader.class).read(p).getOrNull();
                    editor.setEditor(new Editor<>((Tree) tree));
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    private static MutableTreeNode load(Path path) throws IOException {
        var node = new DefaultMutableTreeNode(path.getFileName());
        if (Files.isDirectory(path)) {
            var paths = Vector.ofAll(Files.list(path));
            paths.filter(Files::isDirectory)
                .appendAll(paths.filter(x -> !Files.isDirectory(x)))
                .forEach(p -> {
                    try {
                        node.add(load(p));
                    } catch (IOException ignored) {
                    }
                });
        }
        return node;
    }
}
