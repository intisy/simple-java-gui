package io.github.intisy.gui.listeners;

import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class ScreenListener {
    public static Point mouse;
    public static boolean change = true;
    public static MouseMotionListener getMouseListener(int x, int y) {
        return new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouse = e.getPoint();
                mouse.x += x;
                mouse.y += y;
                change = true;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouse = e.getPoint();
                mouse.x += x;
                mouse.y += y;
                change = true;
            }
        };
    }

    private static final List<JPanel> jPanelList = new ArrayList<>();
    private static final List<JFXPanel> jfxPanelList = new ArrayList<>();

    public static void addMouseListener(JPanel panel, int x, int y) {
        panel.addMouseMotionListener(getMouseListener(x, y));
        jPanelList.add(panel);
    }
    public static void addMouseListener(JFXPanel panel, int x, int y) {
        panel.addMouseMotionListener(getMouseListener(x, y));
        jfxPanelList.add(panel);
    }
    public static void addMouseListener(MouseListener mouseClickListener) {
        for (JPanel panel : jPanelList) {
            panel.addMouseListener(mouseClickListener);
            panel.addMouseListener(mouseClickListener);
        }
        for (JFXPanel panel : jfxPanelList) {
            panel.addMouseListener(mouseClickListener);
            panel.addMouseListener(mouseClickListener);
        }
    }
    public static boolean isSelected(JButton button) {
        if (mouse == null)
            return false;
        return button.getX() <= mouse.x && button.getX()+button.getWidth() > mouse.x && button.getY() <= mouse.y && button.getY()+button.getHeight() > mouse.y;
    }
}
