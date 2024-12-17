package io.github.intisy.gui.presets;

import io.github.intisy.gui.Colors;
import io.github.intisy.gui.SimpleButton;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;

@SuppressWarnings("unused")
public class BlueButton extends SimpleButton {
    public BlueButton(String text, JFXPanel panel, double width, double height, double arc) {
        this(null, text, panel, width, height, arc);
    }
    public BlueButton(Node graphic, JFXPanel panel, double width, double height, double arc) {
        this(graphic, null, panel, width, height, arc);
    }
    public BlueButton(Node graphic, String text, JFXPanel panel, double width, double height, double arc) {
        super(graphic, text, panel, width, height, arc);
        setStrokeWidth(0.7);
        setSelectedStrokeWidth(0.7);
        setBackgroundColor(Colors.selectedStrokeColorBlue);
        setSelectedStrokeColor(Colors.selectedStrokeColorBlue);
        setStrokeColor(Colors.selectedStrokeColorBlue);
        setTextFill(Colors.textColor);
    }
}
