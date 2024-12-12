package io.github.intisy.gui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;

public class MappedJFXPanel extends JFXPanel {

    private MappedParent mappedParent;

    public MappedJFXPanel() {
        Platform.runLater(() -> {
            mappedParent = new MappedParent();
            Scene scene = new Scene(mappedParent);
            setScene(scene);
        });
    }
    public MappedParent getMappedParent() {
        return mappedParent;
    }
}
