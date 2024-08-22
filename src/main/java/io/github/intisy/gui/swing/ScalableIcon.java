package io.github.intisy.gui.swing;

import javax.swing.*;
import java.awt.*;

public class ScalableIcon implements Icon {
    private final Icon icon;
    private final double scale;

    public ScalableIcon(Icon icon, double scale) {
        this.icon = icon;
        this.scale = scale;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(x, y);
        g2d.scale(scale, scale);
        icon.paintIcon(c, g2d, 0, 0);
        g2d.dispose();
    }

    @Override
    public int getIconWidth() {
        return (int) (icon.getIconWidth() * scale);
    }

    @Override
    public int getIconHeight() {
        return (int) (icon.getIconHeight() * scale);
    }
}
