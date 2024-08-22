package io.github.intisy.gui.swing;

import javax.swing.*;
import java.awt.*;

import io.github.intisy.blizzity.GUI;

public class JImageButton extends JHoverButton {
    private final double arcWidth;
    private final double arcHeight;
    public JImageButton(double arcWidth, double arcHeight, ImageIcon icon) {
        super();
        this.setIcon(icon);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        setOpaque(false);
    }
    @Override
    protected void paintComponent(Graphics g, boolean color) {
        int width = getWidth();
        int height = getHeight();
        double multiplier = (double) Math.min(height, width) /40;
        double size = Math.max(getIcon().getIconWidth(), getIcon().getIconHeight())*multiplier;
        g.setColor(GUI.color2);
        g.fillRect(0, 0, width, height);
        if (color)
            g.setColor(this.hoverBoxColor);
        else
            g.setColor(getBackground());
        g.fillRoundRect((int) (width/2-size/2 - 5*multiplier), (int) (height/2-size/2 - 5*multiplier), (int) (size+10*multiplier), (int) (size+10*multiplier), (int) arcWidth, (int) arcHeight);
        Image image = ((ImageIcon) getIcon()).getImage();
        int iconWidth = getIcon().getIconWidth();
        int iconHeight = getIcon().getIconHeight();
        if (multiplier != 1)
            image = image.getScaledInstance((int) (iconWidth*multiplier), (int) (iconHeight*multiplier), Image.SCALE_REPLICATE);
        if (color)
            g.setColor(this.hoverTextColor);
        else
            g.setColor(getForeground());
        g.drawImage(image, (int) (width/2-iconWidth*multiplier/2), (int) (height/2-iconHeight*multiplier/2), this);
    }
}
