/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.display;

import io.vavr.collection.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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

            int d = g.getFontMetrics().getDescent() / 2;
            int height = g.getFontMetrics().getAscent();
            int width = g.getFontMetrics().charWidth('_');

            g.setColor(Color.WHITE);
            g.drawLine(cursor.col * width,
                    cursor.row * height + d,
                    cursor.col * width,
                    cursor.row * height + height + d);

            characters.forEach(c -> {
                g.setColor(c._2.foregroundColor);
                g.drawString(Character.toString(c._2.character),
                        c._1.col * width,
                        c._1.row * height + height);
            });
        }
    }
}
