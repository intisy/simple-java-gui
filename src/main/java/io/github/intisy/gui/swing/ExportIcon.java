package io.github.intisy.gui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class ExportIcon implements Icon {
    private final int width;
    private final int height;
    private final Path2D path;

    public ExportIcon(int width, int height) {
        this.width = width;
        this.height = height;
        this.path = createPath();
    }

    private Path2D createPath() {
        Path2D path = new Path2D.Double();
        double offsetX1 = -0.5;
        double offsetX2 = 0.5;
        // SVG path data translated to Java2D path
        path.moveTo(5.625, 15+offsetX2);
        path.curveTo(5.625, 14.5858+offsetX2, 5.28921, 14.25+offsetX2, 4.875, 14.25+offsetX2);
        path.curveTo(4.46079, 14.25, 4.125+offsetX2, 14.5858+offsetX2, 4.125, 15+offsetX2);
        path.lineTo(5.625, 15+offsetX2);
        path.moveTo(4.875, 16+offsetX2);
        path.lineTo(4.125, 16+offsetX2);
        path.lineTo(4.875, 16+offsetX2);
        path.moveTo(19.275, 15+offsetX2);
        path.curveTo(19.275, 14.5858+offsetX2, 18.9392, 14.25+offsetX2, 18.525, 14.25+offsetX2);
        path.curveTo(18.1108, 14.25+offsetX2, 17.775, 14.5858+offsetX2, 17.775, 15+offsetX2);
        path.lineTo(19.275+offsetX2, 15+offsetX2);
        path.moveTo(12.2914, 5.46127+offsetX1);
        path.curveTo(12.5461, 5.13467+offsetX1, 12.4879, 4.66338+offsetX1, 12.1613, 4.40862+offsetX1);
        path.curveTo(11.8347, 4.15387+offsetX1, 11.3634, 4.21212+offsetX1, 11.1086, 4.53873+offsetX1);
        path.lineTo(12.2914, 5.46127+offsetX1);
        path.moveTo(7.20862, 9.53873+offsetX1);
        path.curveTo(6.95387, 9.86533+offsetX1, 7.01212, 10.3366+offsetX1, 7.33873, 10.5914+offsetX1);
        path.curveTo(7.66533, 10.8461+offsetX1, 8.13662, 10.7879+offsetX1, 8.39138, 10.4613+offsetX1);
        path.lineTo(7.20862, 9.53873+offsetX1);
        path.moveTo(12.2914, 4.53873+offsetX1);
        path.curveTo(12.0366, 4.21212+offsetX1, 11.5653, 4.15387+offsetX1, 11.2387, 4.40862+offsetX1);
        path.curveTo(10.9121, 4.66338+offsetX1, 10.8539, 5.13467+offsetX1, 11.1086, 5.46127+offsetX1);
        path.lineTo(12.2914, 4.53873+offsetX1);
        path.moveTo(15.0086, 10.4613+offsetX1);
        path.curveTo(15.2634, 10.7879+offsetX1, 15.7347, 10.8461+offsetX1, 16.0613, 10.5914+offsetX1);
        path.curveTo(16.3879, 10.3366+offsetX1, 16.4461, 9.86533+offsetX1, 16.1914, 9.53873+offsetX1);
        path.lineTo(15.0086, 10.4613+offsetX1);
        path.moveTo(12.45, 5+offsetX1);
        path.curveTo(12.45, 4.58579+offsetX1, 12.1142, 4.25+offsetX1, 11.7, 4.25+offsetX1);
        path.curveTo(11.2858, 4.25+offsetX1, 10.95, 4.58579+offsetX1, 10.95, 5+offsetX1);
        path.lineTo(12.45, 5+offsetX1);
        path.moveTo(10.95, 16+offsetX1);
        path.curveTo(10.95, 16.4142+offsetX1, 11.2858, 16.75+offsetX1, 11.7, 16.75+offsetX1);
        path.curveTo(12.1142, 16.75+offsetX1, 12.45, 16.4142+offsetX1, 12.45, 16+offsetX1);
        path.lineTo(10.95, 16+offsetX1);
        path.moveTo(4.125, 15+offsetX2);
        path.lineTo(4.125, 16+offsetX2);
        path.lineTo(5.625, 16+offsetX2);
        path.lineTo(5.625, 15+offsetX2);
        path.lineTo(4.125, 15+offsetX2);
        path.moveTo(4.125, 16+offsetX2);
        path.curveTo(4.125, 18.0531+offsetX2, 5.75257, 19.75+offsetX2, 7.8, 19.75+offsetX2);
        path.lineTo(7.8, 18.25+offsetX2);
        path.curveTo(6.61657, 18.25+offsetX2, 5.625, 17.2607+offsetX2, 5.625, 16+offsetX2);
        path.lineTo(4.125, 16+offsetX2);
        path.moveTo(7.8, 19.75+offsetX2);
        path.lineTo(15.6, 19.75+offsetX2);
        path.lineTo(15.6, 18.25+offsetX2);
        path.lineTo(7.8, 18.25+offsetX2);
        path.lineTo(7.8, 19.75+offsetX2);
        path.moveTo(15.6, 19.75+offsetX2);
        path.curveTo(17.6474, 19.75+offsetX2, 19.275, 18.0531+offsetX2, 19.275, 16+offsetX2);
        path.lineTo(17.775, 16+offsetX2);
        path.curveTo(17.775, 17.2607+offsetX2, 16.7834, 18.25+offsetX2, 15.6, 18.25+offsetX2);
        path.lineTo(15.6, 19.75+offsetX2);
        path.moveTo(19.275, 16+offsetX2);
        path.lineTo(19.275, 15+offsetX2);
        path.lineTo(17.775, 15+offsetX2);
        path.lineTo(17.775, 16+offsetX2);
        path.lineTo(19.275, 16+offsetX2);
        path.moveTo(11.1086, 4.53873+offsetX1);
        path.lineTo(7.20862, 9.53873+offsetX1);
        path.lineTo(8.39138, 10.4613+offsetX1);
        path.lineTo(12.2914, 5.46127+offsetX1);
        path.lineTo(11.1086, 4.53873+offsetX1);
        path.moveTo(11.1086, 5.46127+offsetX1);
        path.lineTo(15.0086, 10.4613+offsetX1);
        path.lineTo(16.1914, 9.53873+offsetX1);
        path.lineTo(12.2914, 4.53873+offsetX1);
        path.lineTo(11.1086, 5.46127+offsetX1);
        path.moveTo(10.95, 5+offsetX1);
        path.lineTo(10.95, 16+offsetX1);
        path.lineTo(12.45, 16+offsetX1);
        path.lineTo(12.45, 5+offsetX1);
        path.lineTo(10.95, 5+offsetX1);
        return path;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.translate(x, y);
        g2d.scale((double) width / 24, (double) height / 24);
        g2d.fill(path);
        g2d.dispose();
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }
}