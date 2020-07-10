package io.github.proton.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public final class Display<T> extends JFrame {
    private Editor<T> editor;

    public Display(Editor<T> e) {
        this.editor = e;

        setContentPane(new EditorPanel());
        pack();

        setTitle("Proton");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                editor = editor.update(e);
                getContentPane().repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        repaint();
    }

    private class EditorPanel extends JPanel {
        Font font = new Font("Monospaced", Font.PLAIN, 16);
        int width = 0;

        {
            addComponentListener(new ComponentListener() {
                @Override
                public void componentResized(ComponentEvent e) {
                    editor = new Editor<>(
                        editor.style,
                        editor.projector,
                        (getWidth() / width) - 1, editor.tree,
                        editor.position
                    );
                    repaint();
                }

                @Override
                public void componentMoved(ComponentEvent e) {
                }

                @Override
                public void componentShown(ComponentEvent e) {
                }

                @Override
                public void componentHidden(ComponentEvent e) {
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(720, 480);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(editor.style.background());
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setFont(font);

            var d = (g.getFontMetrics().getDescent() + 1) / 2;
            var height = g.getFontMetrics().getAscent();
            width = g.getFontMetrics().charWidth('_');

            int row = 0, col = 0, i = 0;
            for (Char<T> c : editor.chars()) {
                StyledCharacter character = c.character(editor.style);
                g.setColor(character.foregroundColor());
                g.drawString(Character.toString(character.character()), col * width, row * height + height);
                if (!c.decorative()) {
                    if (i == editor.position) {
                        int cx = col * width, cy = row * height;
                        g.setColor(Color.WHITE);
                        g.drawLine(cx, cy + d, cx, cy + height + d);
                    }
                    i++;
                }
                if (c.character() == '\n') {
                    col = 0;
                    row++;
                } else {
                    col++;
                }
            }
        }
    }
}
