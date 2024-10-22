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
public class SimpleButton extends ButtonBase {
    double height;
    double width;
    private Color strokeColor;
    private Color selectedStrokeColor;
    private Color textFillColor;
    private boolean selected;
    private double strokeWidth;
    private double selectedStrokeWidth;
    private Label label;
    private Node graphic;
    public SimpleButton(String text, JFXPanel panel, double arc) {
        this(text, panel, 100, 30, arc);
    }
    public SimpleButton(String text, JFXPanel panel) {
        this(text, panel, 10);
    }
    public SimpleButton(JFXPanel panel) {
        this("", panel);
    }
    public SimpleButton(String text, JFXPanel panel, double width, double height) {
        this(text, panel, width, height, 10);
    }
    public SimpleButton(String text, JFXPanel panel, double width, double height, double arc) {
        this(null, text, panel, width, height, arc);
    }
    public SimpleButton(Node graphic, JFXPanel panel, double width, double height, double arc) {
        this(graphic, null, panel, width, height, arc);
    }
    public SimpleButton(Node graphic, String text, JFXPanel panel, double width, double height, double arc) {
        super(width, height);
        this.height = height;
        this.width = width;
        this.strokeColor = Colors.strokeColor;
        this.selectedStrokeColor = Colors.selectedStrokeColorBlue;
        this.textFillColor = Colors.textColor;
        this.strokeWidth = 1;
        this.selectedStrokeWidth = 3;
        this.rectangle.setFill(Colors.lightBackgroundColor);
        this.rectangle.setArcWidth(arc);
        this.rectangle.setArcHeight(arc);
        this.rectangle.setStroke(strokeColor);
        this.rectangle.setStrokeWidth(strokeWidth);

        getChildren().add(this.rectangle);
        if (text != null) {
            this.label = new Label(text);
            this.label.setTextFill(textFillColor);
            Font font = new Font(this.label.getFont().getFamily(), this.label.getFont().getSize()*(Math.min(height, width)/22));
            this.label.setFont(font);
            Text fontText = new Text(text);
            fontText.setFont(font);
            this.label.setLayoutY((height - fontText.getBoundsInLocal().getHeight()) /2);
            this.label.setLayoutX((width - fontText.getBoundsInLocal().getWidth()) /2);
            getChildren().add(this.label);
        }
        if (graphic != null) {
            setGraphic(graphic);
        }
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isHover()) {
                    Platform.runLater(() -> hide());
                }
            }
        });
        setOnMouseClicked(event -> {
            if (this.selected) {
                hide();
            } else {
                this.rectangle.setStrokeWidth(selectedStrokeWidth);
                this.rectangle.setStroke(selectedStrokeColor);
                this.selected = true;
            }
            this.onAction.getValue().handle(new ActionEvent());
        });
    }

    public void setGraphic(Node graphic) {
        if (graphic != null)
            getChildren().remove(graphic);
        this.graphic = graphic;
        getChildren().add(graphic);
    }

    public Node getGraphic() {
        return this.graphic;
    }

    public void setText(String text) {
        this.label.setText(text);
    }
    public void setTextFill(Color color) {
        this.label.setTextFill(color);
        this.textFillColor = color;
    }
    public void setBackgroundColor(Color color) {
        this.rectangle.setFill(color);
    }
    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
        this.rectangle.setStroke(strokeColor);
    }
    public void setSelectedStrokeColor(Color selectedStrokeColor) {
        this.selectedStrokeColor = selectedStrokeColor;
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
        this.rectangle.setStroke(strokeColor);
        this.rectangle.setStrokeWidth(strokeWidth);
        this.selected = false;
    }

    public void setSelectedStrokeWidth(double selectedStrokeWidth) {
        this.selectedStrokeWidth = selectedStrokeWidth;
        if (this.selected)
            this.rectangle.setStrokeWidth(selectedStrokeWidth);
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
        if (!this.selected)
            this.rectangle.setStrokeWidth(strokeWidth);
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
}
