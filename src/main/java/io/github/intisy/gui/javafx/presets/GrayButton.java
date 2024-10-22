package io.github.intisy.gui.javafx.presets;

import io.github.intisy.gui.javafx.Colors;
import io.github.intisy.gui.javafx.SimpleButton;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;

public class GrayButton extends SimpleButton {
    public GrayButton(String text, JFXPanel panel, double width, double height, double arc) {
        this(null, text, panel, width, height, arc);
    }
    public GrayButton(Node graphic, JFXPanel panel, double width, double height, double arc) {
        this(graphic, null, panel, width, height, arc);
    }
    public GrayButton(Node graphic, String text, JFXPanel panel, double width, double height, double arc) {
        super(graphic, text, panel, width, height, arc);
        setStrokeWidth(0.7);
        setBackgroundColor(Colors.backgroundColor);
    }
}
