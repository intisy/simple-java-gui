package io.github.intisy.gui.javafx;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class MouseListener implements MouseMotionListener {
    boolean running = false;
    public List<Interface> actions = new ArrayList<>();

    public void addAction(Interface action) {
        actions.add(action);
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    private void callInterface(MouseEvent e) {
        if (running)
            for (Interface action : actions) {
                action.execute(e);
            }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        callInterface(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        callInterface(e);
    }

    @FunctionalInterface
    public interface Interface {
        void execute(MouseEvent event);
    }
}