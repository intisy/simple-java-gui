package io.github.intisy.gui.listeners;

import io.github.intisy.blizzity.GUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginListener extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
        // Check if the clicked point is outside the text field bounds
        if (!GUI.userText.getBounds().contains(e.getPoint())) {
            GUI.userText.transferFocus(); // Transfers focus away from the field
        }
        if (GUI.passText != null && !GUI.passText.getBounds().contains(e.getPoint())) {
            GUI.passText.transferFocus(); // Transfers focus away from the field
        }
    }
}
