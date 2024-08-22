package io.github.intisy.gui.javafx;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;

public class Timeline extends HoverPane {
    double height;
    double width;
    double cursorX;
    public void setWidth(double width) {
        this.width = width;
        render();
    }
    public void setHeight(double height) {
        this.height = height;
        render();
    }
    public Timeline() {
        this(300, 300);
    }
    public Timeline(double width, double height) {
        this.width = width;
        this.height = height;
        render();
    }
    public void render() {
        getChildren().clear();
        Rectangle background = new Rectangle(width, height);
        Path mainPath = new Path();
        mainPath.getElements().add(new MoveTo(2, 0));
        mainPath.getElements().add(new LineTo(6, 0));
        mainPath.getElements().add(new ArcTo(2, 2, 0, 8, 2, false, true));
        mainPath.getElements().add(new LineTo(8, 8));
        mainPath.getElements().add(new ArcTo(4, 4, 0, 0, 8, false, true));
        mainPath.getElements().add(new LineTo(0, 2));
        mainPath.getElements().add(new ArcTo(2, 2, 0, 2, 0, false, true));
        mainPath.getElements().add(new ClosePath());
        Path downPath = new Path();
        downPath.getElements().add(new MoveTo(4, 12));
        downPath.getElements().add(new LineTo(4, height));
        downPath.setStroke(Colors.textColor);
        mainPath.setStroke(Color.WHITE);
        mainPath.setFill(Color.TRANSPARENT);
        mainPath.setStrokeWidth(2);
        Pane cursor = new Pane(mainPath, downPath);
        cursor.setLayoutX(cursorX);
        cursor.setCursor(Cursor.W_RESIZE);
        final double[] clickPoint = new double[1];
        cursor.setOnMousePressed(event -> clickPoint[0] = event.getX());
        cursor.setOnMouseDragged((event) -> {
            double x = (int) (cursor.getLayoutX() - clickPoint[0] + event.getX());
            if (x + cursor.getWidth() <= width && x >= 0)
                cursor.setLayoutX(x);
        });
        getChildren().addAll(background, cursor);
    }
}
