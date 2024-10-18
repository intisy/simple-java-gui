package io.github.intisy.gui.javafx;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.event.MouseAdapter;
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
    private final Label label;
    public SimpleButton(String text, JFXPanel panel, double arc) {
        this(text, panel, 100, 30, arc);
    }
    public SimpleButton(String text, JFXPanel panel) {
        this(text, panel, 100, 30, 10);
    }
    public SimpleButton(JFXPanel panel) {
        this("", panel, 100, 30, 10);
    }
    public SimpleButton(String text, JFXPanel panel, double width, double height) {
        this(text, panel, width, height, 10);
    }
    public SimpleButton(String text, JFXPanel panel, double width, double height, double arc) {
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
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isHover()) {
                    Platform.runLater(() -> hide());
                }
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
    public void setText(String text) {
        label.setText(text);
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
