package io.github.intisy.gui.javafx;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

@SuppressWarnings("unused")
public class SVGButton extends ButtonBase {
    public boolean selected = false;
    public boolean enabled = true;
    private final Node group;
    private Interface onAction;

    public Node getSVG() {
        return group;
    }

    public SVGButton(Node group, double width, double height, double arc) {
        super(width-width/4, height-height/4);
        this.group = group;
        Rectangle hitbox = new Rectangle(0, 0, width, height);
        hitbox.setFill(Color.TRANSPARENT);
        rectangle.setFill(Color.WHITE);
        rectangle.setArcWidth(arc);
        rectangle.setArcHeight(arc);
        rectangle.setLayoutX(width/8);
        rectangle.setLayoutY(height/8);

        getChildren().addAll(group, hitbox);
        hitbox.setOnMouseEntered(event -> {
            if (enabled && !selected && !getChildren().contains(rectangle)) {
                getChildren().add(rectangle);
                group.toFront();
                hitbox.toFront();
            }
        });
        hitbox.setOnMouseExited(event -> {
            if (enabled && !selected && getChildren().contains(rectangle) && (!group.isHover() || !hitbox.isHover() || !rectangle.isHover())) {
                getChildren().remove(rectangle);
                group.toFront();
                hitbox.toFront();
            }

        });
        hitbox.setOnMouseClicked(event -> {
            if (enabled) {
                boolean oldSelected = selected;
                selected = true;
                onAction.execute(oldSelected);
            }
        });
    }
    public void setSelectedAnonymously(boolean selected) {
        this.selected = selected;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
        onAction.execute(selected);
    }
    public final void setOnAction(Interface var1) {
        this.onAction = var1;
    }
    @FunctionalInterface
    public interface Interface {
        void execute(boolean selected);
    }
}
