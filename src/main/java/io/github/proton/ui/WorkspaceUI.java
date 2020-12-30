package io.github.proton.ui;

import io.github.proton.api.editor.EditorReader;
import io.github.proton.plugin.text.TextEditor;
import javafx.scene.layout.BorderPane;
import org.fxmisc.flowless.VirtualizedScrollPane;

import java.io.*;
import java.nio.file.Files;

public final class WorkspaceUI extends BorderPane {
    public WorkspaceUI(File workspace) {
        FileTreeUI fileTreeUI = new FileTreeUI(workspace);

        fileTreeUI.opened.subscribe(file -> {
            try {
                setCenter(new VirtualizedScrollPane<>(new EditorUI(
                    EditorReader.instance.read(file).orElseGet(TextEditor::new),
                    Files.readString(file.toPath())
                )));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        setLeft(fileTreeUI);
    }
}
