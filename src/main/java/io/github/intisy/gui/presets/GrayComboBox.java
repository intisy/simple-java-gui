package io.github.intisy.gui.presets;

import io.github.intisy.gui.Colors;
import io.github.intisy.gui.SimpleComboBox;
import javafx.embed.swing.JFXPanel;

@SuppressWarnings("unused")
public class GrayComboBox<V> extends SimpleComboBox<V> {
    public GrayComboBox(double width, double height, double arc, JFXPanel panel) {
        super(width, height, arc, panel);
        setBackgroundColor(Colors.lightBackgroundColor);
        setSelectedStrokeColor(Colors.selectedStrokeColorBlue);
        setStrokeColor(Colors.strokeColor);
        setTextFill(Colors.textColor);
        setSelectedBackgroundColor(Colors.selectedBackgroundColor);
    }
}
