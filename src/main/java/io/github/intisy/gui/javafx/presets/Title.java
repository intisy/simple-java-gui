package io.github.intisy.gui.javafx.presets;

import io.github.intisy.gui.javafx.Container;
import io.github.intisy.gui.javafx.SimpleSVGButton;
import javafx.scene.Group;
import javafx.scene.shape.SVGPath;

import javax.swing.*;
import java.awt.*;

public class Title extends Container {
    public Title(Frame frame, double width, double height) {
        super(width, height);
        double sizeMultiplier = height / 40;
        final Point[] clickPoint = new Point[1];
        setOnMousePressed(event -> clickPoint[0] = new Point((int) event.getX(), (int) event.getY()));
        setOnMouseDragged((event) -> {
            int xOffset = (int) (frame.getLocation().x - clickPoint[0].x + event.getX());
            int yOffset = (int) (frame.getLocation().y - clickPoint[0].y + event.getY());
            frame.setLocation(xOffset, yOffset);
        });
        SVGPath path1 = new SVGPath();
        path1.setContent("M7 17L16.8995 7.10051");
        path1.setStrokeWidth(0.7);
        path1.setStroke(javafx.scene.paint.Color.WHITE);
        SVGPath path2 = new SVGPath();
        path2.setContent("M7 7.00001L16.8995 16.8995");
        path2.setStrokeWidth(0.7);
        path2.setStroke(javafx.scene.paint.Color.WHITE);
        Group svgGroup = new Group(path1, path2);
        svgGroup.setScaleX(1*sizeMultiplier);
        svgGroup.setScaleY(1*sizeMultiplier);
        svgGroup.setLayoutX(50*sizeMultiplier/2-svgGroup.getLayoutBounds().getCenterX());
        svgGroup.setLayoutY(40*sizeMultiplier/2-svgGroup.getLayoutBounds().getCenterY());
        SimpleSVGButton jfxCloseButton = new SimpleSVGButton(svgGroup, 50*sizeMultiplier, 40*sizeMultiplier);
        jfxCloseButton.setBackgroundColor(javafx.scene.paint.Color.rgb(201,79,79));
        jfxCloseButton.setOnAction(actionEvent -> frame.dispose());
        path1 = new SVGPath();
        path1.setContent("M7 7L16.8995 7.10051");
        path1.setStrokeWidth(0.7);
        path1.setStroke(javafx.scene.paint.Color.WHITE);
        path1.setScaleX(1.2*sizeMultiplier);
        path1.setScaleY(1.2*sizeMultiplier);
        path1.setLayoutX(50*sizeMultiplier/2-path1.getLayoutBounds().getCenterX());
        path1.setLayoutY(40*sizeMultiplier/2-path1.getLayoutBounds().getCenterY());
        SimpleSVGButton jfxMinimizeButton = new SimpleSVGButton(path1, 50*sizeMultiplier, 40*sizeMultiplier);
        jfxMinimizeButton.setBackgroundColor(javafx.scene.paint.Color.rgb(72,75,77));
        jfxMinimizeButton.setOnAction(actionEvent -> frame.setState(JFrame.ICONIFIED));
        getChildren().addAll(jfxMinimizeButton, jfxCloseButton);
        addOnResize((w, h) -> {
            jfxCloseButton.setLayoutX((w - 50*sizeMultiplier));
            jfxMinimizeButton.setLayoutX((w - 100*sizeMultiplier));
        });
        callOnResize(width, height);
        setColor(javafx.scene.paint.Color.rgb(60,63,65));
    }
}
