package io.github.intisy.gui.javafx;

import javafx.scene.layout.Pane;

public class HoverPane extends Pane {
    boolean hovering = false;
    public HoverPane() {
        hoverProperty().addListener((observable, oldValue, newValue) -> hovering = newValue);
    }
}
