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

    public Display(Editor<T> editor) {
        this.editor = editor;
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
                Display.this.editor = Display.this.editor.update(e);
                Display.this.editor.render(Display.this);
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
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(720, 480);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(backgroundColor);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setFont(new Font("Monospaced", Font.PLAIN, 16));

            var d = (g.getFontMetrics().getDescent() + 1) / 2;
            var height = g.getFontMetrics().getAscent();
            var width = g.getFontMetrics().charWidth('_');

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
