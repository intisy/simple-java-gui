package io.github.intisy.gui.javafx;

import io.github.intisy.gui.listeners.ScreenListener;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class AccountComboBox extends HoverPane {
    private final ComboBox<Map<String, String>> internalComboBox;
    private final Rectangle rectangle;
    private Node selection;
    private final List<HoverPane> items = new ArrayList<>();
    private final Polygon arrow;
    double height;
    double width;
    private Map<String, String> selected;
    private HoverPane plusShape;
    private HoverPane selectedPane;
    private final double sizeMultiplier;
    private Color strokeColor = Color.WHITE;
    private Color selectedStrokeColor = Color.BLUE;
    private Color selectedBackground = Color.BLACK;
    public void setBackgroundColor(Color color) {
        rectangle.setFill(color);
    }
    public void setSelectedBackgroundColor(Color color) {
        selectedBackground = color;
    }
    public void setStrokeColor(Color color) {
        strokeColor = color;
        rectangle.setStroke(color);
    }
    public void setSelectedStrokeColor(Color color) {
        selectedStrokeColor = color;
    }
    private HoverPane generateAccountPane(Map<String, String> info) {
        HoverPane pane = new HoverPane();
        if (info != null) {
            Image image = new Image(info.get("avatar"));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            imageView.setLayoutX(10 * sizeMultiplier);
            imageView.setLayoutY(10 * sizeMultiplier);
            Circle clip = new Circle(imageView.getFitHeight() / 2, imageView.getFitHeight() / 2, imageView.getFitHeight() / 2);
            imageView.setClip(clip);
            Label displayName = new Label(info.get("display_name"));
            displayName.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
            displayName.setPrefSize(85 * sizeMultiplier, 20 * sizeMultiplier);
            displayName.setLayoutX(75 * sizeMultiplier);
            displayName.setLayoutY(imageView.getFitHeight() / 2);
            displayName.setTextFill(Color.WHITE);
            Label username = new Label("@" + info.get("username"));
            username.setStyle("-fx-font-weight: bold; -fx-font-size: 9px;");
            username.setPrefSize(85 * sizeMultiplier, 20 * sizeMultiplier);
            username.setLayoutX(75 * sizeMultiplier);
            username.setLayoutY(10 * sizeMultiplier + imageView.getFitHeight() / 2);
            username.setTextFill(Color.rgb(128, 128, 128));
            pane.getChildren().addAll(imageView, displayName, username);
        }
        return pane;
    }
    public AccountComboBox(List<Map<String, String>> data, double width, double height, double arc, boolean auto) {
        sizeMultiplier = width/265;
        internalComboBox = new ComboBox<>();
        internalComboBox.getItems().setAll(data);
        if (auto) {
            for (Map<String, String> entry : data) {
                if (entry.get("selected").equalsIgnoreCase("true")) {
                    selected = internalComboBox.getItems().get(data.indexOf(entry));
                    break;
                }
            }
            internalComboBox.getSelectionModel().select(selected);
        }
        this.height = height;
        this.width = width;
        rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.WHITE);
        rectangle.setArcWidth(arc);
        rectangle.setArcHeight(arc);
        rectangle.setStroke(strokeColor);
        rectangle.setStrokeWidth(1);

        selectedPane = generateAccountPane(selected);

        arrow = new Polygon();
        arrow.getPoints().addAll(
                width-20.0*sizeMultiplier, height/2-2.5*sizeMultiplier,
                width-25.0*sizeMultiplier, height/2+2.5*sizeMultiplier,
                width-30.0*sizeMultiplier, height/2-2.5*sizeMultiplier,
                width-31.0*sizeMultiplier, height/2-1.5*sizeMultiplier,
                width-25.0*sizeMultiplier, height/2+4.5*sizeMultiplier,
                width-19.0*sizeMultiplier, height/2-1.5*sizeMultiplier
        );
        arrow.setFill(Color.rgb(147,163,170));

        getChildren().addAll(rectangle, arrow, selectedPane);
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
            int id = selected == null ? 1 : 0;
            if (internalComboBox.isShowing()) {
                int index = (int) (event.getY() / height);
                if (index < internalComboBox.getItems().size()+1+id) {
                    if (!(index == 0)) {
                        ObservableList<Map<String, String>> items = internalComboBox.getItems();
                        int i = 0;
                        for (Map<String, String> item : items)
                            if (!item.equals(selected)) {
                                if (i == index-1) {
                                    selected = item;
                                    getSelectionModel().select(item);
                                    onSelect.getValue().handle(new ActionEvent());
                                }
                                i++;
                            }
                        if (index == internalComboBox.getItems().size()+id) {
                            onAction.getValue().handle(new ActionEvent());
                        }
                    }
                }
                hide();
            } else {
                double size = 10;
                double thickness = size/5;
                plusShape = new HoverPane();
                Rectangle horizontal = new Rectangle(size, thickness);
                Rectangle vertical = new Rectangle(thickness, size);
                horizontal.relocate(0, (size - thickness) / 2); // Y-position centers the horizontal line
                vertical.relocate((size - thickness) / 2, 0); // X-position centers the vertical line
                horizontal.setFill(Color.WHITE);
                vertical.setFill(Color.WHITE);
                plusShape.getChildren().addAll(horizontal, vertical);
                plusShape.setLayoutY(height*(internalComboBox.getItems().size()+id+0.5)-size/2);
                plusShape.setLayoutX(width/2-size/2);
                getChildren().add(plusShape);
                internalComboBox.show();
                getChildren().remove(arrow);
                rectangle.setStroke(selectedStrokeColor);
                rectangle.setStrokeWidth(3);
                rectangle.setHeight(height*(internalComboBox.getItems().size()+1+id));
                int index = 0;
                for (Map<String, String> item : internalComboBox.getItems()) {
                    if (!item.equals(selected)) {
                        index++;
                        HoverPane itemLabel = generateAccountPane(item);
                        itemLabel.setLayoutY(index * height);
                        itemLabel.setLayoutX(selectedPane.getLayoutX());
                        getChildren().add(itemLabel);
                        items.add(itemLabel);
                    }
                }
                mouseEvent(event);
            }
        });
        getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selected = newValue;
                        getChildren().remove(selectedPane);
                        selectedPane = generateAccountPane(newValue);
                        getChildren().add(selectedPane);
                    }
                });
    }
    public double getNewHeight() {
        return height;
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
    private final ObjectProperty<EventHandler<ActionEvent>> onSelect = new ObjectPropertyBase<EventHandler<ActionEvent>>() {
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
            return "onSelect";
        }
    };
    public final void setOnAction(EventHandler<ActionEvent> var1) {
        this.onAction.set(var1);
    }
    public final void setOnSelect(EventHandler<ActionEvent> var1) {
        this.onSelect.set(var1);
    }
    public double getNewWidth() {
        return width;
    }
    public void mouseEvent(javafx.scene.input.MouseEvent event) {
        int id = selected == null ? 1 : 0;
        if (selection != null)
            getChildren().remove(selection);
        int index = (int) (event.getY() / height);
        if (index <= internalComboBox.getItems().size()+id) {
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
                selectedPane.toFront();
            } else {
                if (index+1 >= internalComboBox.getItems().size()+id) {
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
                if (internalComboBox.getItems().size()+id == index)
                    plusShape.toFront();
                else
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
        getChildren().remove(plusShape);
        for (HoverPane pane : items)
            getChildren().remove(pane);
        if (!getChildren().contains(arrow))
            getChildren().add(arrow);
        items.clear();
    }
    public ObservableList<Map<String, String>> getItems() {
        return internalComboBox.getItems();
    }
    public Map<String, String> getValue() {
        return internalComboBox.getSelectionModel().getSelectedItem();
    }
    public  SingleSelectionModel<Map<String, String>> getSelectionModel() {
        return internalComboBox.getSelectionModel();
    }
}