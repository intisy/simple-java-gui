package io.github.intisy.gui;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.awt.event.MouseAdapter;

@SuppressWarnings("unused")
public class CheckBox extends Pane {
    double width;
    double height;
    private Color backgroundColor = Colors.lightBackgroundColor;
    private Color selectedBackgroundColor = Colors.selectedStrokeColorBlue;
    private Color selectedStrokeColor = Colors.selectedStrokeColorBlue;
    private Color strokeColor = Colors.strokeColor;
    private final Rectangle rectangle;
    private final Rectangle selectedStroke;
    private final Polygon checkmark;
    private boolean selected = false;

    public CheckBox(double width, double height, double arc, ResizablePanel panel) {
        this.width = width;
        this.height = height;
        double checkmarkSize = Math.min(width, height);
        checkmark = new Polygon();
        checkmark.getPoints().addAll(
                0.19 * checkmarkSize, 0.56 * checkmarkSize,
                0.28 * checkmarkSize, 0.47 * checkmarkSize,
                0.40 * checkmarkSize, 0.63 * checkmarkSize,
                0.74 * checkmarkSize, 0.27 * checkmarkSize,
                0.84 * checkmarkSize, 0.34 * checkmarkSize,
                0.44 * checkmarkSize, 0.78 * checkmarkSize
        );
        checkmark.setFill(Color.WHITE);
        checkmark.setScaleX(0.8);
        checkmark.setScaleY(0.8);
        checkmark.setVisible(false);
        selectedStroke = new Rectangle(width, height);
        selectedStroke.setFill(Color.TRANSPARENT);
        selectedStroke.setStroke(selectedStrokeColor);
        selectedStroke.setArcWidth(arc);
        selectedStroke.setArcHeight(arc);
        selectedStroke.setStrokeWidth(2);
        rectangle = new Rectangle(width-4, height-4);
        rectangle.setX(2);
        rectangle.setY(2);
        rectangle.setFill(backgroundColor);
        rectangle.setArcWidth(arc-2);
        rectangle.setArcHeight(arc-2);
        setFocusTraversable(true);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!isHover()) {
                    Platform.runLater(() -> hide());
                }
            }
        });
        setOnMouseClicked(event -> {
            rectangle.setStroke(selectedStrokeColor);
            rectangle.setStrokeWidth(0);
            selectedStroke.setVisible(true);
            if (selected) {
                selected = false;
                rectangle.setFill(backgroundColor);
                checkmark.setVisible(false);
            } else {
                selected = true;
                rectangle.setFill(selectedBackgroundColor);
                checkmark.setVisible(true);
            }
        });
        hide();
        getChildren().addAll(rectangle, selectedStroke, checkmark);
    }

    public void hide() {
        selectedStroke.setVisible(false);
        if (!selected) {
            rectangle.setStroke(strokeColor);
            rectangle.setStrokeWidth(1);
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public Color getSelectedBackgroundColor() {
        return selectedBackgroundColor;
    }

    public void setSelectedBackgroundColor(Color selectedBackgroundColor) {
        this.selectedBackgroundColor = selectedBackgroundColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        rectangle.setFill(backgroundColor);
    }

    public Color getSelectedStrokeColor() {
        return selectedStrokeColor;
    }

    public void setSelectedStrokeColor(Color selectedStrokeColor) {
        this.selectedStrokeColor = selectedStrokeColor;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
        rectangle.setStroke(strokeColor);
    }

    public double getCurrentHeight() {
        return height;
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
    }
}
