/*
package io.github.intisy.gui.swing;

import io.github.intisy.gui.listeners.ScreenListener;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("unused")
public class JHoverButton extends JButton implements MouseMotionListener {
    Color hoverTextColor;
    Color hoverBoxColor;
    protected EventListenerList listenerList = new EventListenerList();
    public void addActionListener(ConditionalEventListener listener) {
        listenerList.add(ConditionalEventListener.class, listener);
    }
    public void removeActionListener(ConditionalEventListener listener) {
        listenerList.remove(ConditionalEventListener.class, listener);
    }
    protected void fireAction(ConditionalEvent event, boolean condition) {
        if (condition) {
            ConditionalEventListener[] listeners = listenerList.getListeners(ConditionalEventListener.class);
            for (ConditionalEventListener listener : listeners) {
                listener.handleEvent(event);
            }
        }
    }
    boolean isDarkened;
    public void setDarkened(boolean darkened) {
        isDarkened = darkened;
        repaint();
    }
    public void setHoverBackground(Color color) {
        this.hoverBoxColor = color;
    }
    public void setHoverForeground(Color color) {
        this.hoverTextColor = color;
    }
    public JHoverButton() {
        super();
        addMouseMotionListener(this);
        super.addActionListener(e -> fireAction(new ConditionalEvent(this), !isDarkened));
    }
    public JHoverButton(Icon icon) {
        super(icon);
        addMouseMotionListener(this);
        super.addActionListener(e -> fireAction(new ConditionalEvent(this), !isDarkened));
    }
    @Override
    protected void paintComponent(Graphics g) {
        try {
            Point mousePoint = MouseInfo.getPointerInfo().getLocation();
            paintComponent(g, !ScreenListener.change && ScreenListener.isSelected(this) && GUI.frame.getBounds().contains(mousePoint) && !isDarkened);
            if (isDarkened) {
                g.setColor(new Color(0, 0, 0, 128));
                g.fillRect(0, 0, Math.max((int) getMaximumSize().getWidth(), getWidth()), Math.max((int) getMaximumSize().getHeight(), getWidth()));
            }
        } finally {
            g.dispose();
        }
    }
    protected void paintComponent(Graphics g, boolean color) {}
    @Override
    public void mouseDragged(MouseEvent e) {
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        ScreenListener.change = false;
        Point point = e.getPoint();
        point.x += getX();
        point.y += getY();
        ScreenListener.mouse = point;
    }
}
*/
