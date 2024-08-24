package io.github.intisy.gui.javafx;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ResizablePanel extends MappedJFXPanel {
    protected double tempWidth, tempHeight, height, width;
    public double offsetRadius;
    double hitboxRadius = 5;
    public List<Interface> resizeEvent = new ArrayList<>();
    JDialog dialog;
    JFrame frame;

    public void setDialog(JDialog dialog) {
        this.dialog = dialog;
    }

    public ResizablePanel(double width, double height, double offsetRadius, JDialog dialog) {
        this(width, height, offsetRadius);
        this.dialog = dialog;
    }
    public ResizablePanel(double width, double height) {
        this(width, height, 0);
    }
    public ResizablePanel() {
        this(0, 0, 0);
    }
    public ResizablePanel(JFrame frame) {
        this(0, 0, 0);
        this.frame = frame;
    }
    public ResizablePanel(double width, double height, double offsetRadius) {
        super();
        this.width = width;
        this.height = height;
        this.offsetRadius = offsetRadius;
        setOpaque(false);
        setBackground(new java.awt.Color(0, 0, 0, 0));
        Platform.runLater(() -> {
            getScene().getRoot().setStyle("-fx-background-color: transparent;");
            getScene().setFill(javafx.scene.paint.Color.TRANSPARENT);
            getMappedParent().add("cursor.nw_resize", newDraggable(Cursor.NW_RESIZE), 0);
            getMappedParent().add("cursor.n_resize", newDraggable(Cursor.N_RESIZE), -1);
            getMappedParent().add("cursor.ne_resize", newDraggable(Cursor.NE_RESIZE), -2);
            getMappedParent().add("cursor.e_resize", newDraggable(Cursor.E_RESIZE), -3);
            getMappedParent().add("cursor.se_resize", newDraggable(Cursor.SE_RESIZE), -4);
            getMappedParent().add("cursor.s_resize", newDraggable(Cursor.S_RESIZE), -5);
            getMappedParent().add("cursor.sw_resize", newDraggable(Cursor.SW_RESIZE), -6);
            getMappedParent().add("cursor.w_resize", newDraggable(Cursor.W_RESIZE), -7);
            moveResizeHitboxes();
        });
    }
    public void setHitboxRadius(double radius) {
        hitboxRadius = radius;
    }
    public void toFront() {
        getMappedParent().toFront("cursor.nw_resize");
        getMappedParent().toFront("cursor.n_resize");
        getMappedParent().toFront("cursor.ne_resize");
        getMappedParent().toFront("cursor.e_resize");
        getMappedParent().toFront("cursor.se_resize");
        getMappedParent().toFront("cursor.s_resize");
        getMappedParent().toFront("cursor.sw_resize");
        getMappedParent().toFront("cursor.w_resize");
    }
    public void setSize(double width, double height) {
        setSize(width, height, true);
    }
    public void setSize(double width, double height, boolean refresh) {
        this.width = width;
        this.height = height;
        moveResizeHitboxes();
        callResize(width, height);
        if (dialog != null) {
            if (refresh)
                dialog.setSize((int) (width + offsetRadius * 2), (int) (height + offsetRadius * 2));
            else {
                if (tempWidth == 0 || Math.abs(tempWidth - width) <= 20) {
                    tempWidth = width + 300;
                    dialog.setSize((int) tempWidth, (int) tempHeight);
                }
                if (tempHeight == 0 || Math.abs(tempHeight - height) <= 20) {
                    tempHeight = height + 300;
                    dialog.setSize((int) tempWidth, (int) tempHeight);
                }
            }
        } else {
                setPreferredSize(new Dimension((int) (width + offsetRadius * 2), (int) (height + offsetRadius * 2)));
                super.setSize(new Dimension((int) (width + offsetRadius * 2), (int) (height + offsetRadius * 2)));
                frame.pack();
        }
    }
    private Rectangle newDraggable(Cursor cursor) {
        Rectangle rectangle = new Rectangle();
        rectangle.setCursor(cursor);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.TRANSPARENT);
        final Point[] clickPoint = new Point[1];
        AtomicReference<Double> initialWidth = new AtomicReference<>(0D);
        AtomicReference<Double> initialHeight = new AtomicReference<>(0D);
        rectangle.setOnMousePressed(event -> {
            initialWidth.set(width);
            initialHeight.set(height);
            clickPoint[0] = new Point((int) event.getX(), (int) event.getY());
            tempWidth = 0;
            tempHeight = 0;
        });
        rectangle.setOnMouseReleased(event -> setFrameSize((int) (width + offsetRadius * 2), (int) (height + offsetRadius * 2)));
        rectangle.setOnMouseDragged(event -> {
            int yOffset = (int) (clickPoint[0].y - event.getY());
            int xOffset = (int) (clickPoint[0].x - event.getX());
            if (cursor.equals(Cursor.NW_RESIZE)) {
                setFrameLocation(getLocationOnScreen().x - xOffset, getLocationOnScreen().y - yOffset); // TODO fix glitchy movement
                setSize(width + xOffset, height + yOffset, false);
            } else if (cursor.equals(Cursor.N_RESIZE)) {
                setFrameLocation(getLocationOnScreen().x, getLocationOnScreen().y - yOffset);
                setSize(width, height + yOffset, false);
            } else if (cursor.equals(Cursor.NE_RESIZE)) {
                setFrameLocation(getLocationOnScreen().x, getLocationOnScreen().y - yOffset);
                setSize(initialWidth.get() - xOffset, height + yOffset, false);
            } else if (cursor.equals(Cursor.E_RESIZE)) {
                setSize(initialWidth.get() - xOffset, height, false);
            } else if (cursor.equals(Cursor.SE_RESIZE)) {
                setSize(initialWidth.get() - xOffset, initialHeight.get() - yOffset, false);
            } else if (cursor.equals(Cursor.S_RESIZE)) {
                setSize(width, initialHeight.get() - yOffset, false);
            } else if (cursor.equals(Cursor.SW_RESIZE)) {
                setFrameLocation(getLocationOnScreen().x - xOffset, getLocationOnScreen().y);
                setSize(width + xOffset, initialHeight.get() - yOffset, false);
            } else if (cursor.equals(Cursor.W_RESIZE)) {
                setFrameLocation(getLocationOnScreen().x - xOffset, getLocationOnScreen().y);
                setSize(width + xOffset, height, false);
            }
        });
        return rectangle;
    }

    @Override
    public Point getLocationOnScreen() {
        if (dialog != null) {
            return dialog.getLocation();
        } else {
            return super.getLocationOnScreen();
        }
    }

    public void setFrameLocation(int x, int y) {
        if (dialog != null) {
            dialog.setLocation(x, y);
        } else {
            frame.setLocation(new Point(x, y));
        }
    }
    private void setFrameSize(int width, int height) {
        if (dialog != null) {
            dialog.setSize(width, height);
        } else {
            setPreferredSize(new Dimension(width, height));
            frame.pack();
        }
    }
    public void moveResizeHitboxes() {
        Rectangle topLeftHitbox = (Rectangle) getMappedParent().get("cursor.nw_resize");
        topLeftHitbox.setX(offsetRadius - hitboxRadius);
        topLeftHitbox.setY(offsetRadius - hitboxRadius);
        topLeftHitbox.setWidth(hitboxRadius *2);
        topLeftHitbox.setHeight(hitboxRadius *2);
        Rectangle topHitbox = (Rectangle) getMappedParent().get("cursor.n_resize");
        topHitbox.setX(offsetRadius + hitboxRadius);
        topHitbox.setY(offsetRadius - hitboxRadius);
        topHitbox.setWidth(width- hitboxRadius *2);
        topHitbox.setHeight(hitboxRadius *2);
        Rectangle topRightHitbox = (Rectangle) getMappedParent().get("cursor.ne_resize");
        topRightHitbox.setX(width+ offsetRadius - hitboxRadius);
        topRightHitbox.setY(offsetRadius - hitboxRadius);
        topRightHitbox.setWidth(hitboxRadius *2);
        topRightHitbox.setHeight(hitboxRadius *2);
        Rectangle rightHitbox = (Rectangle) getMappedParent().get("cursor.e_resize");
        rightHitbox.setX(width+ offsetRadius - hitboxRadius);
        rightHitbox.setY(offsetRadius + hitboxRadius);
        rightHitbox.setWidth(hitboxRadius *2);
        rightHitbox.setHeight(height- hitboxRadius *2);
        Rectangle bottomRightHitbox = (Rectangle) getMappedParent().get("cursor.se_resize");
        bottomRightHitbox.setX(width+ offsetRadius - hitboxRadius);
        bottomRightHitbox.setY(height+ offsetRadius - hitboxRadius);
        bottomRightHitbox.setWidth(hitboxRadius *2);
        bottomRightHitbox.setHeight(hitboxRadius *2);
        Rectangle bottomHitbox = (Rectangle) getMappedParent().get("cursor.s_resize");
        bottomHitbox.setX(offsetRadius + hitboxRadius);
        bottomHitbox.setY(height+ offsetRadius - hitboxRadius);
        bottomHitbox.setWidth(width- hitboxRadius *2);
        bottomHitbox.setHeight(hitboxRadius *2);
        Rectangle bottomLeftHitbox = (Rectangle) getMappedParent().get("cursor.sw_resize");
        bottomLeftHitbox.setX(offsetRadius - hitboxRadius);
        bottomLeftHitbox.setY(height+ offsetRadius - hitboxRadius);
        bottomLeftHitbox.setWidth(hitboxRadius *2);
        bottomLeftHitbox.setHeight(hitboxRadius *2);
        Rectangle leftHitbox = (Rectangle) getMappedParent().get("cursor.w_resize");
        leftHitbox.setX(offsetRadius - hitboxRadius);
        leftHitbox.setY(offsetRadius + hitboxRadius);
        leftHitbox.setWidth(hitboxRadius *2);
        leftHitbox.setHeight(height- hitboxRadius *2);
    }
    public void callResize(double width, double height) {
        for (Interface action : resizeEvent) {
            action.execute(width, height);
        }
    }
    public void addOnResize(Interface action) {
        resizeEvent.add(action);
    }
    @FunctionalInterface
    public interface Interface {
        void execute(double width, double height);
    }
}
