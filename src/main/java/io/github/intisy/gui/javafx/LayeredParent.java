package io.github.intisy.gui.javafx;

import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.*;

public class LayeredParent extends Parent {
    Map<Integer, Node> children = new HashMap<>();
    public int getFreeKey() {
        Set<Integer> keys = children.keySet();
        if (keys.isEmpty())
            return 1;
        return Collections.max(keys)+1;
    }
    public void add(Node node) {
        children.put(getFreeKey(), node);
        getChildren().add(node);
    }
    public void add(Node node, int layer) {
        if (!children.containsKey(layer)) {
            children.put(layer, node);
            getChildren().clear();
            Map<Integer, Node> sortedMap = new TreeMap<>(Comparator.naturalOrder());
            sortedMap.putAll(children);
            for (Node child : sortedMap.values()) {
                getChildren().add(child);
            }
        }
    }
    public void remove(int layer) {
        children.remove(layer);
    }
    public void remove(Node node) {
        children.entrySet().removeIf(entry -> entry.getValue().equals(node));
    }
}
