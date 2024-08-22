package io.github.intisy.gui.swing;

import javax.swing.*;
import java.awt.*;

public class MovableIcon implements Icon {
    private final Icon icon;
    private final int x;
    private final int y;

    public MovableIcon(Icon icon, int x, int y) {
        this.icon = icon;
        this.x = x;
        this.y = y;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(x, y);
        icon.paintIcon(c, g2d, this.x, this.y);
        g2d.dispose();
    }

    @Override
    public int getIconWidth() {
        return icon.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return icon.getIconHeight();
    }
}
