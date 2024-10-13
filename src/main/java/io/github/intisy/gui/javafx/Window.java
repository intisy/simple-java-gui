package io.github.intisy.gui.javafx;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.List;

public class Window {
    double outlineRadius = 1;
    int iconSize = 10;
    double blurRadius;
    double height;
    double width;
    double sizeMultiplier;
    JDialog dialog;
    ResizablePanel jfxPanel;
    SimpleSVGButton jfxCloseButton;

    public JDialog getDialog() {
        return dialog;
    }

    public ResizablePanel getJfxPanel() {
        return jfxPanel;
    }

    public Window() {
        this(300, 200, 10);
    }
    public Window(double width, double height, double blurRadius) {
        this.width = width;
        this.height = height;
        this.blurRadius = blurRadius;
        sizeMultiplier = blurRadius / 10;
    }

    public void open(JFrame frame) {
        jfxPanel = new ResizablePanel(width, height, blurRadius);
        SwingUtilities.invokeLater(() -> {
            dialog = new JDialog(frame, "", Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setUndecorated(true);
            dialog.setSize((int) (width+blurRadius*2), (int) (height+blurRadius*2));
            dialog.setLocationRelativeTo(frame);
            dialog.setBackground(new java.awt.Color(0, 0, 0, 0));
            dialog.getContentPane().setBackground(new java.awt.Color(0, 0, 0, 0));
            dialog.getRootPane().setOpaque(false);
            dialog.getRootPane().setBackground(new java.awt.Color(0, 0, 0, 0));
            jfxPanel.setDialog(dialog);
            jfxPanel.addOnResize((width, height) -> {
                Rectangle divider = (Rectangle) jfxPanel.getMappedParent().get("window.divider");
                divider.setWidth(width);
                Rectangle outline = (Rectangle) jfxPanel.getMappedParent().get("window.outline");
                outline.setWidth(width+outlineRadius*2);
                outline.setHeight(height+outlineRadius*2);
                Rectangle blur = (Rectangle) jfxPanel.getMappedParent().get("window.blur");
                blur.setHeight(height);
                blur.setWidth(width);
                blur.toBack();
                Container main = (Container) jfxPanel.getMappedParent().get("window.main");
                main.setCurrentHeight(height - 40 * sizeMultiplier, true);
                main.setCurrentWidth(width, true);
                Container title = (Container) jfxPanel.getMappedParent().get("window.title");
                title.setCurrentWidth(width, true);
                jfxCloseButton.setLayoutX((width - 30)*sizeMultiplier);
            });
            setContent(dialog);
            dialog.add(jfxPanel);
            dialog.setVisible(true);
        });
    }
    public void setContent(JDialog dialog) {
        Platform.runLater(() -> {
            Rectangle outline = new Rectangle(blurRadius - outlineRadius, blurRadius - outlineRadius, width + outlineRadius * 2, height + outlineRadius * 2);
            outline.setFill(Color.TRANSPARENT);
            Rectangle divider = new Rectangle(blurRadius, blurRadius + 40 * sizeMultiplier, width, 1);
            divider.setFill(Color.rgb(60, 63, 65));
            Rectangle rectangle = new Rectangle(blurRadius, blurRadius, width, height);
            rectangle.setFill(Color.BLACK);
            GaussianBlur blur = new GaussianBlur();
            blur.setRadius(blurRadius);
            rectangle.setEffect(blur);
            Container title = new Container(width, 40 * sizeMultiplier)
                    .setY(blurRadius)
                    .setX(blurRadius)
                    .setColor(Color.rgb(43, 45, 48));
            dialog.addWindowFocusListener(new WindowFocusListener() {
                @Override
                public void windowGainedFocus(WindowEvent e) {
                    Platform.runLater(() -> {
                        title.setColor(Color.rgb(43, 45, 48));
                        outline.setFill(Color.TRANSPARENT);
                    });
                }

                @Override
                public void windowLostFocus(WindowEvent e) {
                    Platform.runLater(() -> {
                        title.setColor(Color.rgb(60, 63, 65));
                        outline.setFill(Color.rgb(60, 63, 65));
                        outline.toBack();
                        rectangle.toBack();
                    });
                }
            });
            Group closeShape = new Group(
                    new Line(0, 0, iconSize, iconSize),
                    new Line(0, iconSize, iconSize, 0)
            );
            closeShape.setLayoutX((30 * sizeMultiplier - iconSize) / 2);
            closeShape.setLayoutY((40 * sizeMultiplier - iconSize) / 2);
            closeShape.getChildren().forEach(line -> ((Line) line).setStroke(Color.WHITE));
            jfxCloseButton = new SimpleSVGButton(closeShape, 30 * sizeMultiplier, 40 * sizeMultiplier);
            jfxCloseButton.setBackgroundColor(Color.rgb(201, 79, 79));
            jfxCloseButton.setLayoutX((width - 30) * sizeMultiplier);
            jfxCloseButton.setOnAction(actionEvent -> close());
            title.getChildren().add(jfxCloseButton);
            final Point[] clickPoint = new Point[1];
            title.setOnMousePressed(event -> clickPoint[0] = new Point((int) event.getX(), (int) event.getY()));
            title.setOnMouseDragged(event -> {
                int xOffset = (int) (dialog.getLocation().x - clickPoint[0].x + event.getX());
                int yOffset = (int) (dialog.getLocation().y - clickPoint[0].y + event.getY());
                dialog.setLocation(xOffset, yOffset);
            });
            Container main = new Container(width, height - 41 * sizeMultiplier).setY(41 * sizeMultiplier + blurRadius).setX(blurRadius);
            jfxPanel.getMappedParent().addAll("window.blur", rectangle, "window.outline", outline, "window.divider", divider, "window.title", title, "window.main", main);
            jfxPanel.toFront();
        });
    }
    public void close() {
        dialog.dispose();
        callOnClose();
    }
    public ObservableList<Node> getChildren() {
        return ((Container) jfxPanel.getMappedParent().get("window.main")).getChildren();
    }
    public Container getContainer() {
        return (Container) jfxPanel.getMappedParent().get("window.main");
    }
    public List<Interface> onClose = new ArrayList<>();
    public void addOnClose(Interface action) {
        onClose.add(action);
    }
    public void callOnClose() {
        for (Interface action : onClose) {
            action.execute();
        }
    }

    @FunctionalInterface
    public interface Interface {
        void execute();
    }
}
