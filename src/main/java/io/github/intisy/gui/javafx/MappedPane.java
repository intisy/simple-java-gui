package io.github.intisy.gui.javafx;

import javafx.collections.MapChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class MappedPane extends Pane {
    private final ObservableMap<String, Node> children = new ObservableMapWrapper<>(new HashMap<>());
    public MappedPane() {
        children.addListener((MapChangeListener<String, Node>) change -> {
            if (change.wasAdded()) {
                getChildren().add(change.getValueAdded());
            }
            if (change.wasRemoved()) {
                getChildren().remove(change.getValueAdded());
            }
        });
    }
    public ObservableMap<String, Node> getChildrenMap() {
        return children;
    }
}
