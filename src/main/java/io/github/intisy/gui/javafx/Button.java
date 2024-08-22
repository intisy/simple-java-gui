package io.github.intisy.gui.javafx;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Button extends HoverPane {
    public Rectangle rectangle;
    public void setBackgroundColor(Color color) {
        rectangle.setFill(color);
    }
    public double width;
    public double height;
    Button(double width, double height) {
        rectangle = new Rectangle(width, height);
        setWidth(width);
        setWidth(height);
    }
}
