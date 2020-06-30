package io.github.proton.editor;

import io.vavr.collection.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public final class Display<T> extends JFrame {
    private Editor<T> editor;
    private Color backgroundColor;
    private Map<Position, StyledCharacter> characters;
    private Position cursor;

    public Display(Editor<T> e) {
        this.editor = e;
        editor.render(this);

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
                editor.render(Display.this);
                getContentPane().repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        repaint();
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setCharacters(Map<Position, StyledCharacter> characters) {
        this.characters = characters;
    }

    public void setCursor(Position cursor) {
        this.cursor = cursor;
    }

    private class EditorPanel extends JPanel {
        Font font = new Font("Monospaced", Font.PLAIN, 16);
        int width = 0;

        {
            addComponentListener(new ComponentListener() {
                @Override
                public void componentResized(ComponentEvent e) {
                    editor = new Editor<>(
                        editor.style(),
                        editor.projector(),
                        editor.tree(),
                        editor.cursor(),
                        (getWidth() / width) - 1);
                    editor.render(Display.this);
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

            g.setColor(backgroundColor);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setFont(font);

            var d = (g.getFontMetrics().getDescent() + 1) / 2;
            var height = g.getFontMetrics().getAscent();
            width = g.getFontMetrics().charWidth('_');

            var cx = cursor.col() * width;
            var cy = cursor.row() * height;
            g.setColor(Color.WHITE);
            g.drawLine(cx, cy + d, cx, cy + height + d);

            characters.forEach(c -> {
                g.setColor(c._2.foregroundColor());
                g.drawString(Character.toString(c._2.character()), c._1.col() * width, c._1.row() * height + height);
            });
        }
    }
}
