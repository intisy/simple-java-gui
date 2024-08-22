package io.github.intisy.gui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JTransparentButton extends JButton {
    boolean pressed;
    Color pressedColor;
    Color pressedBoxColor;
    public JTransparentButton(String text) {
        super(text);
        setOpaque(false);
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                repaint();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                repaint();
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    public void setPressedColor(Color pressed, Color pressedBox) {
        this.pressedColor = pressed;
        this.pressedBoxColor = pressedBox;
    }

    @Override
    protected void paintComponent(Graphics g) {
        try {
            int width = getWidth();
            int height = getHeight();
            String text = getText();

            // Calculate baseline for text drawing to align text properly
            int baseline = getBaseline(width, height);

            if (pressed) {
                g.setColor(pressedBoxColor);
                g.fillRoundRect(0, 0, getFontMetrics(getFont()).stringWidth(getText()), getFontMetrics(getFont()).getHeight(), 2, 2);
                g.setColor(pressedColor);
            } else
                g.setColor(getForeground());
            g.drawString(text, 0, baseline);
        } finally {
            g.dispose();
        }
    }
}
