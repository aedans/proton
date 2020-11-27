package io.github.proton.ui;

import javafx.scene.control.*;
import org.reactfx.EventSource;

import java.io.File;
import java.util.Objects;

public final class FileTreeUI extends TreeView<File> {
    public final EventSource<File> opened = new EventSource<>();

    public FileTreeUI(File file) {
        super(open(file));

        setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && getSelectionModel().getSelectedItem() != null && getSelectionModel().getSelectedItem().isLeaf()) {
                opened.push(getSelectionModel().getSelectedItem().getValue());
            }
        });

        setCellFactory(param -> new TreeCell<>() {
            @Override
            protected void updateItem(File f, boolean empty) {
                super.updateItem(f, empty);
                if (f != null) {
                    setText(f.getName());
                } else {
                    setText("");
                }
            }
        });
    }

    private static TreeItem<File> open(File file) {
        TreeItem<File> item = new TreeItem<>(file);
        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                item.getChildren().add(open(f));
            }
        }
        return item;
    }
}
