package io.github.intisy.gui.javafx;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("unused")
public class BasicButton extends ButtonBase {
    double height;
    double width;
    private Color textFillColor;
    private Color backgroundColor;
    private Color selectedBackgroundColor;
    private boolean selected;
    private Label label;
    public BasicButton(String text) {
        this(text, 100, 30);
    }
    public BasicButton() {
        this(null);
    }
    public BasicButton(String text, double width, double height) {
        super(width, height);
        this.height = height;
        this.width = width;
        this.textFillColor = Colors.textColor;
        this.backgroundColor = Colors.backgroundColor;
        this.selectedBackgroundColor = Colors.selectedBackgroundColorBlue;
        this.rectangle.setFill(backgroundColor);

        getChildren().add(this.rectangle);
        if (text != null) {
            this.label = new Label(text);
            this.label.setTextFill(textFillColor);
            label.widthProperty().addListener((observable, oldValue, newValue) -> {
                Font font = this.label.getFont();
                double size = font.getSize() * width / newValue.doubleValue() * 0.8;
                if (size < font.getSize())
                    setFontSize(size);
            });
            label.heightProperty().addListener((observable, oldValue, newValue) -> {
                Font font = this.label.getFont();
                double size = font.getSize() * height / newValue.doubleValue() * 0.8;
                if (size < font.getSize())
                    setFontSize(size);
            });
            getChildren().add(this.label);
        }
        setOnMouseClicked(event -> {
            if (this.selected) {
                hide();
            } else {
                this.rectangle.setFill(selectedBackgroundColor);
                this.selected = true;
            }
            this.onAction.getValue().handle(new ActionEvent());
        });
    }

    public void setText(String text) {
        this.label.setText(text);
    }
    public void setTextFill(Color color) {
        this.label.setTextFill(color);
        this.textFillColor = color;
    }
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        if (!selected)
            this.rectangle.setFill(backgroundColor);
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

    public void setSelectedBackgroundColor(Color selectedBackgroundColor) {
        this.selectedBackgroundColor = selectedBackgroundColor;
        if (selected)
            this.rectangle.setFill(selectedBackgroundColor);
    }

    public void hide() {
        this.rectangle.setFill(backgroundColor);
        this.selected = false;
    }

    public final void setOnAction(EventHandler<ActionEvent> var1) {
        this.onAction.set(var1);
    }
    public final Font getFont() {
        return this.label.getFont();
    }
    public final void setFont(Font font) {
        this.label.setFont(font);
        Text fontText = new Text(this.label.getText());
        fontText.setFont(this.label.getFont());
        this.label.setLayoutY((this.height - fontText.getBoundsInLocal().getHeight()) /2);
        this.label.setLayoutX((this.width - fontText.getBoundsInLocal().getWidth()) /2);
    }
    public final void setFontSize(double size) {
        setFont(new Font(this.label.getFont().getFamily(), size));
    }
}
