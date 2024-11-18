package io.github.intisy.gui.javafx;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

@SuppressWarnings("unused")
public class Switch extends Region {
    public boolean selected;
    public Color background = Colors.lightBackgroundColor;
    public Color activated = Colors.selectedStrokeColorBlue;
    public Color foreground = Colors.textColor;
    public double width;
    public double height;
    public Switch(double width, double height) {
        setPrefSize(width, height);
        updateStyle(width, height);
        this.width = width;
        this.height = height;
        setOnMouseClicked(event -> {
            selected = !selected;
            updateStyle(width, height);
        });
    }

    public void setBackground(Color background) {
        this.background = background;
        updateStyle(width, height);
    }
    public void setForeground(Color foreground) {
        this.foreground = foreground;
        updateStyle(width, height);
    }
    public void setActivated(Color activated) {
        this.activated = activated;
        updateStyle(width, height);
    }

    private void updateStyle(double width, double height) {
        // Create a rectangle
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setArcHeight(height);
        rectangle.setArcWidth(height);

        // Create a circle
        Circle circle;
        Circle circleShadow;
        double minimize = 2.4;
        if (selected) {
            circle = new Circle(width - height / 2, height / 2, height / minimize);
            circleShadow = new Circle(width - height / 2, height - height / (minimize-0.2), height / minimize);
            rectangle.setFill(activated);
        } else {
            circle = new Circle(height / 2, height / 2, height / minimize);
            circleShadow = new Circle(height / 2, height - height / (minimize-0.2), height / minimize);
            rectangle.setFill(background);
        }
        circleShadow.setFill(mixColors(foreground, background));
        circle.setFill(foreground);

        // Add shapes to the custom object
        getChildren().clear();
        getChildren().addAll(rectangle, circleShadow, circle);
    }
    public static Color mixColors(Color color1, Color color2) {
        // Calculate the average of the RGB components
        double red = (color1.getRed() + color2.getRed()) / 2.0;
        double green = (color1.getGreen() + color2.getGreen()) / 2.0;
        double blue = (color1.getBlue() + color2.getBlue()) / 2.0;

        // Create and return the mixed color
        return new Color(red, green, blue, 1.0); // Alpha value is set to 1.0 for fully opaque color
    }
}
