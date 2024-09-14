package io.github.intisy.gui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@SuppressWarnings("unused")
public class JRoundedTextField extends JTextField {
    private final int arcWidth;
    private final int arcHeight;
    private int alignmentOffset = 40;
    private Point dragStart;
    private int l;
    private String previewText;
    private Color previewColor;
    private int w;
    private boolean blink;
    private UUID uuid;
    public JRoundedTextField(int arcWidth, int arcHeight, int columns) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        setColumns(columns);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
        FocusListener focusListener = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
                UUID luuid = UUID.randomUUID();
                uuid = luuid;
                blink = true;
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        // Your code to be executed every second
                        if (luuid == uuid) {
                            repaint();
                            blink = !blink;
                        } else
                            timer.cancel();
                    }
                };

                // Schedule the task to run every second with no initial delay
                timer.schedule(task, 0, 500);
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
                uuid = null;
            }
        };
        MouseAdapter mouseHandler = new MouseAdapter() { //TODO improve selection system
            @Override
            public void mousePressed(MouseEvent e) {
//                Point point = e.getPoint();
//                point.x -= alignmentOffset;
//                int clickOffset = viewToModel2D(point);
//                dragStart = point;
//                adjustCaretPosition(clickOffset);
//                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
//                Point point = e.getPoint();
//                point.x -= alignmentOffset;
//                int dragEnd = viewToModel2D(point);
//                if (viewToModel2D(dragStart) != -1 && dragEnd != -1) {
//                    select(Math.min(viewToModel2D(dragStart), dragEnd), Math.max(viewToModel2D(dragStart), dragEnd));
//                    String selected = getSelectedText();
//                    if (selected != null) {
//                        l = getFontMetrics(getFont()).stringWidth(getText().substring(0, Math.min(viewToModel2D(dragStart), dragEnd))) + alignmentOffset;
//                        w = getFontMetrics(getFont()).stringWidth(selected);
//
//                        repaint();
//                    }
//                }
            }
            private void adjustCaretPosition(int clickOffset) {
                int textLength = getText().length();
                int adjustedOffset = Math.min(Math.max(clickOffset, 0), textLength);
                setCaretPosition(adjustedOffset);
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addFocusListener(focusListener);
    }
    public int getAlignmentOffset() {
        return alignmentOffset;
    }
    public void setAlignmentOffset(int alignmentOffset) {
        this.alignmentOffset = alignmentOffset;
        repaint();
    }
    public String getPreviewText() {
        return previewText;
    }
    public void setPreviewText(String text, Color color) {
        this.previewText = text;
        this.previewColor = color;
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        try {
            Insets insets = getInsets();

            int width = getWidth();
            int height = getHeight();
            String text = getText();

            // Calculate baseline for text drawing to align text properly
            int baseline = getBaseline(width, height);

            // Set the color for drawing text, considering selection
            if (getSelectedText() != null) {
                g.setColor(getBackground());
                g.fillRoundRect(insets.left, insets.top, getWidth() - insets.left - insets.right, getHeight() - insets.top - insets.bottom, arcWidth, arcHeight);
                g.setColor(getSelectionColor());
                g.fillRect(l, getHeight()/2-getFontMetrics(getFont()).getHeight()/2, w, getFontMetrics(getFont()).getHeight());
                g.setColor(getForeground());
                // Draw the text with the custom alignment
                g.drawString(text, insets.left + alignmentOffset, baseline);
                g.setColor(getSelectedTextColor());
                g.drawString(getSelectedText(), l, baseline);
            } else {
                g.setColor(getBackground());
                g.fillRoundRect(insets.left, insets.top, getWidth() - insets.left - insets.right, getHeight() - insets.top - insets.bottom, arcWidth, arcHeight);
                // Draw the text with the custom alignment
                if (text == null || text.isEmpty()) {
                    if (!hasFocus()) {
                        g.setColor(previewColor);
                        g.drawString(previewText, insets.left + alignmentOffset, baseline);
                    }
                } else {
                    g.setColor(getForeground());
                    g.drawString(text, insets.left + alignmentOffset, baseline);
                }
            }
            if (hasFocus() && getSelectedText() == null) {
                if (!blink) {
                    g.setColor(getForeground());
                    g.fillRect(getFontMetrics(getFont()).stringWidth(getText().substring(0, getCaretPosition())) + alignmentOffset, getHeight() / 2 - getFontMetrics(getFont()).getHeight() / 2, 2, getFontMetrics(getFont()).getHeight());
                }
            }
        } finally {
            g.dispose();
        }
    }
}