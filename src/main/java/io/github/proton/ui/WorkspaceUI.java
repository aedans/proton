package io.github.proton.ui;

import io.github.proton.plugin.java.*;
import io.github.proton.plugin.lisp.LispEditor;
import javafx.scene.layout.BorderPane;
import org.fxmisc.flowless.VirtualizedScrollPane;

import java.io.*;
import java.nio.file.Files;

public final class WorkspaceUI extends BorderPane {
    public WorkspaceUI(File file) {
        FileTreeUI fileTreeUI = new FileTreeUI(file);

        fileTreeUI.opened.subscribe((f) -> {
            try {
                setCenter(new VirtualizedScrollPane<>(new EditorUI(
                    new JavaEditor(JavaLanguageServer.INSTANCE, f),
                    Files.readString(f.toPath())
                )));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        setLeft(fileTreeUI);
    }
}
