package io.github.intisy.gui.javafx;

@SuppressWarnings("unused")
public class ResizeEvent {
    boolean canceled = false;
    double width;
    double height;
    double lastWidth;
    double lastHeight;
    public ResizeEvent(double width, double height, double lastWidth, double lastHeight) {
        this.width = width;
        this.height = height;
        this.lastWidth = lastWidth;
        this.lastHeight = lastHeight;
    }

    public double getLastHeight() {
        return lastHeight;
    }

    public double getLastWidth() {
        return lastWidth;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
