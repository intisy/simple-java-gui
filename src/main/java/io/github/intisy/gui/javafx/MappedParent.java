package io.github.intisy.gui.javafx;

import io.github.intisy.gui.Double;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;

import java.util.*;

@SuppressWarnings("unused")
public class MappedParent extends LayeredParent {
    private final ObservableMap<String, Double<Integer, Node>> children = FXCollections.observableHashMap();
    public MappedParent() {
        children.addListener((MapChangeListener<String, Double<Integer, Node>>) change -> {
            if (change.wasAdded()) {
                add(change.getValueAdded().getValue(), change.getValueAdded().getKey());
            }
            if (change.wasRemoved()) {
                remove(change.getValueRemoved().getValue());
            }
        });
    }
    public void toFront(String key) {
        Double<Integer, Node> value = children.get(key);
        children.remove(key);
        add(key, value.getValue());
    }
    public void add(String key, Node child) {
        add(key, child, getFreeKey());
    }
    public void add(String key, Node child, int layer) {
        if (children.containsKey(key)) {
            throw new IllegalArgumentException("Key already exists: " + key);
        }
        children.put(key, new Double<>(layer, child));
    }
    public void renderOutline() {
        ObservableMap<String, Double<Integer, Node>> childrenClone = FXCollections.observableHashMap();
        childrenClone.putAll(children);
        childrenClone.forEach((name1, child1) -> {
            if (child1.getValue() instanceof Container) {
                childrenClone.forEach((name2, child2) -> {
                    if (child2.getValue() instanceof Container) {
                        if (((Container) child1.getValue()).hasRelatedY((Container) child2.getValue())) {
                            ((Container) child1.getValue()).setOutline(Container.TOP, true, name1, this, ((Container) child2.getValue()).resizable);
                        }
                        if (((Container) child1.getValue()).hasRelatedX((Container) child2.getValue())) {
                            ((Container) child1.getValue()).setOutline(Container.RIGHT, true, name1, this, ((Container) child2.getValue()).resizable);
                        }
                    }
                });
            }
        });
    }
    public void addAll(Object... objects) {
        for (int i = 0; true; i+=2) {
            if (i >= objects.length)
                break;
            add((String) objects[i], (Node) objects[i+1]);
        }
    }

    public Node get(String key) {
        if (children.containsKey(key))
            return children.get(key).getValue();
        else
            return null;
    }
    public void remove(String node) {
        children.remove(node);
    }
    @Deprecated
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }

    public Map<String, Node> getChildrenMap() {
        Map<String, Double<Integer, Node>> sortedMap = new TreeMap<>(Comparator.reverseOrder());
        sortedMap.putAll(children);
        Map<String, Node> values = new HashMap<>();
        sortedMap.forEach((name, value) -> values.put(name, value.getValue()));
        return values;
    }

    @Override
    public ObservableList<Node> getChildrenUnmodifiable() {
        Map<String, Double<Integer, Node>> sortedMap = new TreeMap<>(Comparator.reverseOrder());
        sortedMap.putAll(children);
        List<Node> values = new ArrayList<>();
        sortedMap.forEach((name, value) -> values.add(value.getValue()));
        return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(values));
    }
}
