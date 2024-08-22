package io.github.intisy.gui.javafx;

public interface ObservableMap<K, V> extends javafx.collections.ObservableMap<K, V> {
    public void putAll(Object... values);
}
