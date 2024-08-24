package io.github.intisy.gui.swing;

import java.awt.*;

public class JSimpleButton extends JHoverButton {
    public JSimpleButton(Frame frame, String text) {
        super(frame);
        setText(text);
        setOpaque(false);
    }
    @Override
    protected void paintComponent(Graphics g, boolean color) {
        int width = getWidth();
        int height = getHeight();
        String text = getText();
        double multiplier = (double) Math.min(height, width) /40;

        // Calculate baseline for text drawing to align text properly
        int baseline = getBaseline(width, height);
        if (color)
            g.setColor(hoverBoxColor);
        else
            g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        if (color)
            g.setColor(hoverTextColor);
        else
            g.setColor(getForeground());
        double size = 10*multiplier;
        if (text.equals("X")) {
            // Draw the "X" lines
            g.drawLine((int) (width / 2 - size / 2), (int) (height / 2 - size / 2), (int) (width / 2 + size / 2), (int) (height / 2 + size / 2));
            g.drawLine((int) (width / 2 - size / 2), (int) (height / 2 + size / 2), (int) (width / 2 + size / 2), (int) (height / 2 - size / 2));
        } else if (text.equals("-")) {
            // Draw the "-" lines
            g.drawLine((int) (width / 2 - size / 2), height / 2, (int) (width / 2 + size / 2), height / 2);
        } else {
            g.drawString(text, getWidth()/2-getFontMetrics(getFont()).stringWidth(getText())/2, baseline);
        }
    }
}
