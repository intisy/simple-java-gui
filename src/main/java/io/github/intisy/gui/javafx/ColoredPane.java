package io.github.intisy.gui.javafx;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class ColoredPane extends Pane {
    private Color color;

    public ColoredPane(Color color) {
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
        for (Node child : getChildren()) {
            if (child instanceof Shape) {
                ((Shape) child).setFill(color);
            }
        }
    }

    public Color getColor() {
        return color;
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        for (Node child : getChildren()) {
            if (child instanceof Shape) {
                ((Shape) child).setFill(color);
            }
        }
    }
}
