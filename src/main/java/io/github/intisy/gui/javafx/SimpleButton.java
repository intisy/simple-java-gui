package io.github.intisy.gui.javafx;

import io.github.intisy.gui.listeners.ScreenListener;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@SuppressWarnings("unused")
public class SimpleButton extends Button {
    double height;
    double width;
    private Color strokeColor = Colors.strokeColor;
    private Color selectedStrokeColor = Colors.selectedStrokeColorBlue;
    private Color textFillColor = Colors.textColor;
    private boolean selected;
    private Label label;
    public SimpleButton(String text, double arc) {
        this(text, 100, 30, arc);
    }
    public SimpleButton(String text) {
        this(text, 100, 30, 10);
    }
    public SimpleButton() {
        this("", 100, 30, 10);
    }
    public SimpleButton(String text, double width, double height) {
        this(text, width, height, 10);
    }
    public SimpleButton(String text, double width, double height, double arc) {
        super(width, height);
        this.height = height;
        this.width = width;
        rectangle.setFill(Colors.lightBackgroundColor);
        rectangle.setArcWidth(arc);
        rectangle.setArcHeight(arc);
        rectangle.setStroke(strokeColor);
        rectangle.setStrokeWidth(1);

        label = new Label(text);
        label.setTextFill(textFillColor);
        Font font = new Font(label.getFont().getFamily(), label.getFont().getSize()*(Math.min(height, width)/22));
        label.setFont(font);
        Text fontText = new Text(text);
        fontText.setFont(font);
        label.setLayoutY((height - fontText.getBoundsInLocal().getHeight()) /2);
        label.setLayoutX((width - fontText.getBoundsInLocal().getWidth()) /2);

        getChildren().addAll(rectangle, label);
        ScreenListener.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!isHover()) {
                    Platform.runLater(() -> hide());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        setOnMouseClicked(event -> {
            if (selected) {
                hide();
            } else {
                rectangle.setStrokeWidth(3);
                rectangle.setStroke(selectedStrokeColor);
                selected = true;
            }
            onAction.getValue().handle(new ActionEvent());
        });
    }
    public void setTextFill(Color color) {
        label.setTextFill(color);
        textFillColor = color;
    }
    public void setBackgroundColor(Color color) {
        rectangle.setFill(color);
    }
    public void setStrokeColor(Color color) {
        strokeColor = color;
        rectangle.setStroke(color);
    }
    public void setSelectedStrokeColor(Color color) {
        selectedStrokeColor = color;
    }
    private final ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() {
        @Override
        protected void invalidated() {
            setEventHandler(ActionEvent.ACTION, get());
        }

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "onAction";
        }
    };
    public void hide() {
        rectangle.setStroke(strokeColor);
        rectangle.setStrokeWidth(1);
        selected = false;
    }
    public final void setOnAction(EventHandler<ActionEvent> var1) {
        this.onAction.set(var1);
    }
    public final Font getFont() {
        return label.getFont();
    }
    public final void setFont(Font font) {
        label.setFont(font);
        Text fontText = new Text(label.getText());
        fontText.setFont(label.getFont());
        label.setLayoutY((height - fontText.getBoundsInLocal().getHeight()) /2);
        label.setLayoutX((width - fontText.getBoundsInLocal().getWidth()) /2);
    }
}
