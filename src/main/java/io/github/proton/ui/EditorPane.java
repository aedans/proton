package io.github.proton.ui;

import io.github.proton.editor.*;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public final class EditorPane<T> extends JTextPane {
    private Editor<T> editor;
    private int width = 0;

    public EditorPane(Editor<T> editor) {
        this.editor = editor;
    }

    {
        setFont(new Font("Monospaced", Font.PLAIN, 16));
        setInputMap(WHEN_FOCUSED, null);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                editor = editor.update(e);
                render();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                editor = new Editor<>(
                    editor.style,
                    editor.projector,
                    (getWidth() / width) - 1,
                    editor.tree,
                    editor.index
                );
                render();
            }
        });

        addCaretListener(e -> {
            editor = editor.select(e.getDot());
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(720, 480);
    }

    @Override
    public void paint(Graphics g) {
        width = g.getFontMetrics().charWidth('_');
        super.paint(g);
    }

    private void render() {
        var editor = this.editor;
        setText(editor.chars.map(Char::character).mkString());
        setCaretPosition(Editor.selectedIndex(editor.chars, editor.index));
        setBackground(editor.style.background());
        setCaretColor(Color.WHITE);

        for (int i = 0; i < editor.chars.size(); i++) {
            var c = editor.chars.get(i);
            AttributeSet attributeSet = StyleContext.getDefaultStyleContext().addAttribute(
                StyleContext.getDefaultStyleContext().getEmptySet(), StyleConstants.Foreground, c.character(editor.style).foregroundColor()
            );
            getStyledDocument().setCharacterAttributes(i, 1, attributeSet, true);
        }
    }
}