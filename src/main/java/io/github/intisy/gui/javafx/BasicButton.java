package io.github.intisy.gui.javafx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

@SuppressWarnings("unused")
public class BasicButton extends ButtonBase {
    double height;
    double width;
    private Color textFillColor;
    private Color backgroundColor;
    private Color selectedBackgroundColor;
    private boolean selected;
    private Label label;
    private Double fontHeight;
    private Double fontWidth;
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
                fontWidth = size;
                setFontSize(size);
            });
            label.heightProperty().addListener((observable, oldValue, newValue) -> {
                Font font = this.label.getFont();
                double size = font.getSize() * height / newValue.doubleValue() * 0.8;
                fontHeight = size;
                setFontSize(size);
            });
            getChildren().add(this.label);
        }
        setOnMouseClicked(event -> {
            this.rectangle.setFill(selectedBackgroundColor);
            this.selected = true;
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
        if (fontHeight != null && fontWidth != null) {
            Text fontText = new Text(this.label.getText());
            fontText.setFont(this.label.getFont());
            double fontSize = Math.min(fontHeight, fontWidth);
            this.label.setLayoutY((this.height - fontSize * height / font.getSize()) / 2);
            this.label.setLayoutX((this.width - fontSize * width / font.getSize()) / 2);
            this.label.setFont(font);
            System.out.println(fontHeight);
            System.out.println(fontWidth);
            System.out.println(fontSize);
            System.out.println(fontSize * height / font.getSize());
            System.out.println(fontSize * width / font.getSize());
            fontWidth = null;
            fontHeight = null;
        }
    }
    public final void setFontSize(double size) {
        setFont(new Font(this.label.getFont().getFamily(), size));
    }
}
