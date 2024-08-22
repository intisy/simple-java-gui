package io.github.intisy.gui.javafx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

@SuppressWarnings("unused")
public class SVGButton extends Button {
    public boolean selected = false;
    private final Node group;
    private final ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<>() {
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
    public SVGButton(Node group, double width, double height, double arc) {
        super(width-width/4, height-height/4);
        this.group = group;
        Rectangle hitbox = new Rectangle(0, 0, width, height);
        hitbox.setFill(javafx.scene.paint.Color.TRANSPARENT);
        rectangle.setFill(Color.WHITE);
        rectangle.setArcWidth(arc);
        rectangle.setArcHeight(arc);
        rectangle.setLayoutX(width/8);
        rectangle.setLayoutY(height/8);

        getChildren().addAll(group, hitbox);
        hitbox.setOnMouseEntered(event -> {
            if (!selected && !getChildren().contains(rectangle)) {
                getChildren().add(rectangle);
                group.toFront();
                hitbox.toFront();
            }
        });
        hitbox.setOnMouseExited(event -> {
            if (!selected && getChildren().contains(rectangle) && (!group.isHover() || !hitbox.isHover() || !rectangle.isHover())) {
                getChildren().remove(rectangle);
                group.toFront();
                hitbox.toFront();
            }

        });
        hitbox.setOnMouseClicked(event -> {
            onAction.getValue().handle(new ActionEvent());
            selected = true;
        });
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            if (!getChildren().contains(rectangle)) {
                getChildren().add(rectangle);
                group.toFront();
            }
        } else {
            getChildren().remove(rectangle);
        }

    }
    public void callEvent() {
        onAction.getValue().handle(new ActionEvent());
    }
    public final void setOnAction(EventHandler<ActionEvent> var1) {
        this.onAction.set(var1);
    }
}
