package io.github.intisy.gui;

import javafx.collections.MapChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

@SuppressWarnings("unused")
public class MappedPane extends Pane {
    private final ObservableMap<String, Node> children = new ObservableMapWrapper<>();
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
