package io.github.intisy.gui;

public interface ObservableMap<K, V> extends javafx.collections.ObservableMap<K, V> {
    void putAll(Object... values);
}
