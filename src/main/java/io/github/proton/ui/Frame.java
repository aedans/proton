package io.github.proton.ui;

import io.github.proton.editor.Editor;

import javax.swing.*;
import java.awt.*;

public final class Frame<T> extends JFrame {
    public Frame(Editor<T> editor) {
        setLayout(new BorderLayout());
        add(new EditorPane<>(editor), BorderLayout.CENTER);
        pack();

        setTitle("Proton");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
