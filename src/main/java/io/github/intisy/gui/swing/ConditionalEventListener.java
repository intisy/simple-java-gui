package io.github.intisy.gui.swing;

import java.util.EventListener;

public interface ConditionalEventListener extends EventListener {
    void handleEvent(ConditionalEvent event);
}
