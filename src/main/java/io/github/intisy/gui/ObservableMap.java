package io.github.intisy.gui;

@SuppressWarnings("unused")
public interface ObservableMap<K, V> extends javafx.collections.ObservableMap<K, V> {
    void putAll(Object... values);
}
