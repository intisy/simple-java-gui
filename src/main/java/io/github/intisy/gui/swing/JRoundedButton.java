package io.github.intisy.gui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JRoundedButton extends JButton {
    boolean pressed;
    Color pressedColor;
    Color pressedBoxColor;
    private final int arcWidth;
    private final int arcHeight;
    public JRoundedButton(int arcWidth, int arcHeight, String text) {
        super(text);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
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
            } else
                g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
            if (pressed) {
                g.setColor(pressedColor);
            } else
                g.setColor(getForeground());
            g.drawString(text, getWidth()/2-getFontMetrics(getFont()).stringWidth(getText())/2, baseline);
        } finally {
            g.dispose();
        }
    }
}
