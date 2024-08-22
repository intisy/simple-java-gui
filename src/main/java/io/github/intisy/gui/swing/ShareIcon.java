package io.github.intisy.gui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class ShareIcon implements Icon {
    private final int width;
    private final int height;
    private final Path2D path;

    public ShareIcon(int width, int height) {
        this.width = width;
        this.height = height;
        this.path = createPath();
    }

    private Path2D createPath() {
        Path2D path = new Path2D.Double();
        // SVG path data translated to Java2D path
        path.moveTo(8.5, 4);
        path.curveTo(8.77614, 4, 9, 4.22386, 9, 4.5);
        path.curveTo(9, 4.74545778, 8.82312296, 4.9496079, 8.58987501, 4.99194425);
        path.lineTo(8.5, 5);
        path.lineTo(5.5, 5);
        path.curveTo(4.72030118, 5, 4.079551, 5.59488554, 4.00686655, 6.35553954);
        path.lineTo(4, 6.5);
        path.lineTo(4, 14.5);
        path.curveTo(4, 15.2796706, 4.59488554, 15.9204457, 5.35553954, 15.9931332);
        path.lineTo(5.5, 16);
        path.lineTo(13.5, 16);
        path.curveTo(14.2796706, 16, 14.9204457, 15.4050879, 14.9931332, 14.6444558);
        path.lineTo(15, 14.5);
        path.lineTo(15, 13.5);
        path.curveTo(15, 13.2239, 15.2239, 13, 15.5, 13);
        path.curveTo(15.7454222, 13, 15.9496, 13.1769086, 15.9919429, 13.4101355);
        path.lineTo(16, 13.5);
        path.lineTo(16, 14.5);
        path.curveTo(16, 15.825472, 14.9684531, 16.9100378, 13.6643744, 16.9946823);
        path.lineTo(13.5, 17);
        path.lineTo(5.5, 17);
        path.curveTo(4.1745184, 17, 3.08996147, 15.9684531, 3.00531769, 14.6643744);
        path.lineTo(3, 14.5);
        path.lineTo(3, 6.5);
        path.curveTo(3, 5.1745184, 4.03153766, 4.08996147, 5.33562452, 4.00531769);
        path.lineTo(5.5, 4);
        path.lineTo(8.5, 4);
        path.closePath();

        path.moveTo(12.3776, 3.57782);
        path.curveTo(12.3776, 3.13003467, 12.8394631, 2.87507849, 13.1939008, 3.06178145);
        path.lineTo(13.2679992, 3.10864);
        path.lineTo(13.3297, 3.16165);
        path.lineTo(17.8268, 7.58283);
        path.curveTo(18.0134, 7.76623444, 18.0492, 8.04847543, 17.9342667, 8.26983565);
        path.lineTo(17.8841, 8.34968);
        path.lineTo(17.8269, 8.41512);
        path.lineTo(13.3298, 12.8377);
        path.curveTo(13.01452, 13.1477533, 12.5151742, 12.9826156, 12.4012138, 12.5911573);
        path.lineTo(12.3829, 12.5039);
        path.lineTo(12.3776, 12.4216);
        path.lineTo(12.3776, 10.3261);
        path.curveTo(12.1287, 10.3527, 11.8813, 10.3915, 11.6353, 10.4425);
        path.curveTo(10.10152, 10.7608, 8.62218, 11.5547, 7.19031, 12.8332);
        path.curveTo(6.80088, 13.181, 6.19837, 12.852, 6.26242, 12.3267);
        path.curveTo(6.74867, 8.33874525, 8.74473514, 6.09593837, 12.1462729, 5.72418393);
        path.lineTo(12.4107, 5.69928);
        path.lineTo(12.6353, 5.68417);
        path.lineTo(12.3776, 3.57782);
        path.closePath();

        path.moveTo(13.3776, 4.611);
        path.lineTo(13.3776, 6.64252);
        path.lineTo(12.2263, 6.71996);
        path.curveTo(10.6526, 6.84601, 9.49235, 7.39422, 8.6632, 8.28848);
        path.curveTo(7.99417, 9.01004, 7.48493, 10.01736, 7.18674, 11.35546);
        path.curveTo(8.60226133, 10.2946333, 10.0973151, 9.6298312, 11.6743974, 9.39794109);
        path.lineTo(12.022, 9.35399);
        path.lineTo(13.3776, 9.23219);
        path.lineTo(13.3776, 11.3882);
        path.lineTo(16.8238, 8.00001);
        path.lineTo(13.3776, 4.611);
        path.closePath();

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
        g2d.setStroke(new BasicStroke(0.1f));
        g2d.draw(path);
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