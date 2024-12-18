package io.github.intisy.gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

@SuppressWarnings("unused")
public class ButtonBase extends HoverPane {
    public Rectangle rectangle;
    public Color color;
    public double width;
    public double height;
    public ButtonBase(double width, double height) {
        rectangle = new Rectangle(width, height);
        setWidth(width);
        setWidth(height);
        this.width = width;
        this.height = height;
    }

    public void setBackgroundColor(Color color) {
        rectangle.setFill(color);
        this.color = color;
    }

    public Color getBackgroundColor() {
        return color;
    }

    public double getCurrentHeight() {
        return this.height;
    }

    public double getCurrentWidth() {
        return this.width;
    }
}
