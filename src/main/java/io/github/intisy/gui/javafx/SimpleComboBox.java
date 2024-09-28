package io.github.intisy.gui.javafx;

import io.github.intisy.gui.listeners.ScreenListener;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
public class SimpleComboBox<T> extends Pane {

    private ComboBox<T> internalComboBox;
    private Rectangle rectangle;
    private Node selection;
    private Label selected;
    private final List<Label> items = new ArrayList<>();
    private Polygon arrow;
    double height;
    double width;
    private Color strokeColor = Colors.strokeColor;
    private Color selectedStrokeColor = Colors.selectedStrokeColorBlue;
    private Color textFillColor = Colors.textColor;
    private Color selectedBackground = Colors.selectedBackgroundColorBlue;

    public SimpleComboBox() {
        initializeComboBox(100, 30, 10);
    }
    public SimpleComboBox(double width, double height) {
        initializeComboBox(width, height, 10);
    }
    public SimpleComboBox(double width, double height, double arc) {
        initializeComboBox(width, height, arc);
    }
    public void setBackgroundColor(Color color) {
        rectangle.setFill(color);
    }
    public void setSelectedBackgroundColor(Color color) {
        selectedBackground = color;
    }
    public void setTextFill(Color color) {
        selected.setTextFill(color);
        textFillColor = color;
    }
    public void setStrokeColor(Color color) {
        strokeColor = color;
        rectangle.setStroke(color);
    }
    public void setSelectedStrokeColor(Color color) {
        selectedStrokeColor = color;
    }
    private void initializeComboBox(double width, double height, double arc) {
        internalComboBox = new ComboBox<>();
        this.height = height;
        this.width = width;
        double multiplier = Math.min(height, width)/25;
        rectangle = new Rectangle(width, height);
        rectangle.setFill(Colors.lightBackgroundColor);
        rectangle.setArcWidth(arc);
        rectangle.setArcHeight(arc);
        rectangle.setStroke(strokeColor);
        rectangle.setStrokeWidth(1);

        selected = new Label();
        Font font = new Font(selected.getFont().getFamily(), selected.getFont().getSize()*multiplier);
        selected.setFont(font);
        selected.setLayoutX(10*multiplier);

        arrow = new Polygon();
        arrow.getPoints().addAll(
                width-10.0*multiplier, height/2-2.5*multiplier,
                width-15.0*multiplier, height/2+2.5*multiplier,
                width-20.0*multiplier, height/2-2.5*multiplier,
                width-21.0*multiplier, height/2-1.5*multiplier,
                width-15.0*multiplier, height/2+4.5*multiplier,
                width-9.0*multiplier, height/2-1.5*multiplier
        );
        arrow.setFill(Color.rgb(147,163,170));

        getChildren().addAll(rectangle, arrow, selected);
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
        setOnMouseMoved(event -> {
            if (internalComboBox.isShowing()) {
                mouseEvent(event);
            }
        });
        setOnMouseClicked(event -> {
            int id = selected.getText().isEmpty() ? 1 : 0;
            if (internalComboBox.isShowing()) {
                int index = (int) (event.getY() / height);
                if (index < internalComboBox.getItems().size()+id) {
                    if (!(index == 0)) {
                        ObservableList<String> items = (ObservableList<String>) internalComboBox.getItems();
                        int i = 0;
                        for (String item : items)
                            if (!item.equals(selected.getText())) {
                                if (i == index-1) {
                                    selected.setText(item);
                                    getSelectionModel().select((T) item);
                                }
                                i++;
                            }
                    }
                }
                hide();
            } else {
                internalComboBox.show();
                getChildren().remove(arrow);
                rectangle.setStroke(selectedStrokeColor);
                rectangle.setStrokeWidth(3);
                rectangle.setHeight(height*(internalComboBox.getItems().size()+id));
                int index = 0;
                for (T item : internalComboBox.getItems()) {
                    if (!item.equals(selected.getText())) {
                        index++;
                        Label itemLabel = new Label((String) item);
                        itemLabel.setTextFill(textFillColor);
                        itemLabel.setFont(font);
                        Text newFont = new Text((String) item);
                        newFont.setFont(font);
                        itemLabel.setLayoutY((height - newFont.getBoundsInLocal().getHeight()) /2 + index * height);
                        itemLabel.setLayoutX(selected.getLayoutX());
                        getChildren().add(itemLabel);
                        items.add(itemLabel);
                    }
                }
                mouseEvent(event);
            }
        });
        getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    Text newFont = new Text((String) newValue);
                    newFont.setFont(selected.getFont());
                    selected.setLayoutY((height - newFont.getBoundsInLocal().getHeight()) /2);
                    selected.setText((String) newValue);
                });
    }
    public double getNewHeight() {
        return height;
    }
    public double getNewWidth() {
        return width;
    }
    public void mouseEvent(javafx.scene.input.MouseEvent event) {
        int id = selected.getText().isEmpty() ? 1 : 0;
        if (selection != null)
            getChildren().remove(selection);
        int index = (int) (event.getY() / height);
        if (index < internalComboBox.getItems().size()+id) {
            int indent = 3;
            double arc = 2.5;
            Path path = new Path();
            path.setFill(selectedBackground);
            path.setStroke(selectedBackground);
            if (index == 0) {
                path.getElements().add(new MoveTo(indent, indent + arc));
                path.getElements().add(new ArcTo(arc, arc, 45, indent + arc, indent, false, true));
                path.getElements().add(new LineTo(width - indent - arc, indent));
                path.getElements().add(new ArcTo(arc, arc, 45, width - indent, indent + arc, false, true));
                path.getElements().add(new LineTo(width - indent, height));
                path.getElements().add(new LineTo(indent, height));
                path.getElements().add(new ClosePath());
                selection = path;
                getChildren().add(selection);
                selected.toFront();
            } else {
                if (index+1 == internalComboBox.getItems().size()+id) {
                    path.getElements().add(new MoveTo(indent, index * height));
                    path.getElements().add(new LineTo(width - indent, index * height));
                    path.getElements().add(new LineTo(width - indent, (index+1) * height - arc - indent));
                    path.getElements().add(new ArcTo(arc, arc, 45, width - arc - indent, (index+1) * height - indent, false, true));
                    path.getElements().add(new LineTo(arc + indent, (index+1) * height - indent));
                    path.getElements().add(new ArcTo(arc, arc, 45, indent, (index+1) * height - arc - indent, false, true));
                    path.getElements().add(new ClosePath());
                    selection = path;
                    getChildren().add(selection);
                } else {
                    Rectangle rectangle = new Rectangle(width - indent*2, height);
                    rectangle.setLayoutY(index * height + indent);
                    rectangle.setLayoutX(indent);
                    rectangle.setFill(selectedBackground);
                    selection = rectangle;
                    getChildren().add(selection);
                }
                items.get(index - 1).toFront();
            }
        }
    }
    public void hide() {
        if (selection != null)
            getChildren().remove(selection);
        internalComboBox.hide();
        rectangle.setStroke(strokeColor);
        rectangle.setStrokeWidth(1);
        rectangle.setHeight(height);
        for (Label label : items)
            getChildren().remove(label);
        if (!getChildren().contains(arrow))
            getChildren().add(arrow);
        items.clear();
    }
    public ObservableList<T> getItems() {
        return internalComboBox.getItems();
    }
    public T getValue() {
        return internalComboBox.getSelectionModel().getSelectedItem();
    }
    public  SingleSelectionModel<T> getSelectionModel() {
        return internalComboBox.getSelectionModel();
    }
}