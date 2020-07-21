package io.github.proton.ui;

import io.github.proton.editor.*;
import io.github.proton.plugins.Plugins;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public final class EditorComponent extends JTextPane {
    private Editor<Object> editor;
    private int width = 0;

    public EditorComponent(Editor<Object> editor) {
        this.editor = editor;
    }

    {
        for (Object o : getActionMap().allKeys()) {
            System.out.println(o);
        }

        getActionMap().put(getInputMap().get(KeyStroke.getKeyStroke(' ')), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var c = e.getActionCommand().charAt(0);
                if (c >= ' ' && c != 127) {
                    setEditor(editor.insert(e.getActionCommand().charAt(0)));
                }
            }
        });

        getActionMap().put("caret-backward", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEditor(editor.left());
            }
        });

        getActionMap().put("caret-forward", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEditor(editor.right());
            }
        });

        getActionMap().put("caret-up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEditor(editor.up());
            }
        });

        getActionMap().put("caret-down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEditor(editor.down());
            }
        });

        getActionMap().put("insert-break", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEditor(editor.enter());
            }
        });

        getActionMap().put("delete-previous", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEditor(editor.backspace());
            }
        });

        getActionMap().put("delete-previous-word", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getActionMap().get("selection-previous-word").actionPerformed(e);
                getActionMap().get("delete-previous").actionPerformed(e);
            }
        });

        getActionMap().put("delete-next", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEditor(editor.delete());
            }
        });

        getActionMap().put("delete-next-word", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getActionMap().get("selection-next-word").actionPerformed(e);
                getActionMap().get("delete-next").actionPerformed(e);
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setEditor(new Editor<>(
                    editor.projector,
                    ((getWidth() - 2) / Math.max(1, width)) - 2,
                    editor.tree,
                    editor.dot,
                    editor.mark
                ));
            }
        });

        addCaretListener(e -> {
            editor = editor.select(e.getDot(), e.getMark());
        });
    }

    @Override
    public void paint(Graphics g) {
        width = g.getFontMetrics().charWidth('_');
        super.paint(g);
    }

    public void setEditor(Editor<Object> editor) {
        this.editor = editor;
        render();
    }

    private void render() {
        var editor = this.editor;
        setText(editor.chars.init().map(Char::character).mkString());
        setCaretPosition(Editor.selectedIndex(editor.chars, editor.dot));
        setCaretColor(Color.WHITE);

        var theme = Plugins.getTheme();
        for (int i = 0; i < editor.chars.size(); i++) {
            var c = editor.chars.get(i);
            AttributeSet attributeSet = StyleContext.getDefaultStyleContext().addAttribute(
                StyleContext.getDefaultStyleContext().getEmptySet(), StyleConstants.Foreground, theme.color(c.style())
            );
            getStyledDocument().setCharacterAttributes(i, 1, attributeSet, true);
        }
    }
}