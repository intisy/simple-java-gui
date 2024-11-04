package io.github.intisy.gui.javafx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.lang.Double;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class BasicButton extends ButtonBase {
    double height;
    double width;
    private Color textFillColor;
    private Color backgroundColor;
    private Color selectedBackgroundColor;
    private boolean selected;
    private boolean expanded;
    private Font font;
    private final List<Label> labels = new ArrayList<>();
    private final List<Rectangle> rectangles = new ArrayList<>();
    private java.lang.Double fontHeight;
    private String[] texts;
    private Double fontWidth;
    public BasicButton(String... texts) {
        this(100, 30, texts);
    }
    public BasicButton() {
        this(new String[] {});
    }
    public BasicButton(double width, double height, String... texts) {
        super(width, height);
        setHeight(height);
        setWidth(width);
        this.height = height;
        this.width = width;
        this.textFillColor = Colors.textColor;
        this.backgroundColor = Colors.backgroundColor;
        this.selectedBackgroundColor = Colors.selectedBackgroundColorBlue;
        this.rectangle.setFill(Color.TRANSPARENT);
        this.texts = texts;
        setCurrentTexts(texts[0]);

        getChildren().add(this.rectangle);
        setOnMouseClicked(event -> {
            if (!selected)
                select();
            else {
                setCurrentTexts(texts);
            }
        });
    }

    public void setCurrentTexts(String... texts) {
        getChildren().clear();
        labels.clear();
        rectangles.clear();
        int index = 0;
        for (String text : texts) {
            Rectangle rectangle = new Rectangle(width, height);
            rectangle.setTranslateY(height * index);
            Label label = new Label(text);
            label.setTextFill(textFillColor);
            Font font = new Font(label.getFont().getFamily(), label.getFont().getSize()*(Math.min(height, width)/22));
            label.setFont(font);
            Text fontText = new Text(text);
            fontText.setFont(font);
            label.setLayoutY((height * (1 + index) - fontText.getBoundsInLocal().getHeight()) /2);
            label.setLayoutX((width - fontText.getBoundsInLocal().getWidth()) /2);
            labels.add(label);
            rectangles.add(rectangle);
            getChildren().addAll(label, rectangle);
            index++;
        }
    }

    public void setTexts(String... texts) {
        this.texts = texts;
    }
    public void setTextFill(Color color) {
        this.textFillColor = color;
    }
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        if (!selected)
            for (Rectangle rectangle : rectangles)
                rectangle.setFill(backgroundColor);
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
            for (Rectangle rectangle : rectangles)
                rectangle.setFill(selectedBackgroundColor);
    }

    public void select() {
        for (Rectangle rectangle : rectangles)
            rectangle.setFill(selectedBackgroundColor);
        this.selected = true;
        this.onAction.getValue().handle(new ActionEvent());
    }

    public void hide() {
        for (Rectangle rectangle : rectangles)
            rectangle.setFill(backgroundColor);
        this.selected = false;
    }

    public final void setXOffset(double xOffset) {
        for (Label label : labels) {
            label.setLayoutX(xOffset);
        }
    }

    public final void setOnAction(EventHandler<ActionEvent> var1) {
        this.onAction.set(var1);
    }
    public final Font getFont() {
        return this.font;
    }
    public final void setFont(Font font) {
        for (Label label : labels) {
            label.setFont(font);
            Text fontText = new Text(label.getText());
            fontText.setFont(font);
            label.setLayoutY((this.height - fontText.getBoundsInLocal().getHeight()) / 2);
            label.setLayoutX((this.width - fontText.getBoundsInLocal().getWidth()) / 2);
            this.font = font;
        }
    }
}
