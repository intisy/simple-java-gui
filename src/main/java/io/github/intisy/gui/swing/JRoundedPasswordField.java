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
public class JRoundedPasswordField extends JTextField {
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
    public JRoundedPasswordField(int arcWidth, int arcHeight, int columns) {
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

        MouseAdapter mouseHandler = getMouseAdapter();

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addFocusListener(focusListener);
    }

    private MouseAdapter getMouseAdapter() { //TODO fix selection system
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                point.x -= alignmentOffset;
                int clickOffset = (point.x+6)/10;
                dragStart = point;
                adjustCaretPosition(clickOffset);
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point point = e.getPoint();
                point.x -= alignmentOffset;
                int dragEnd = (point.x+6)/10;

                if ((dragStart.x+6)/10 != -1 && dragEnd >= 0) {
                    select(Math.min((dragStart.x+6)/10, dragEnd), Math.max((dragStart.x+6)/10, dragEnd));
                    String selected = getSelectedText();
                    if (selected != null) {
                        l = 10*Math.min((dragStart.x+6)/10, dragEnd) + alignmentOffset;
                        w = 10*selected.length();

                        repaint();
                    }
                }
            }
            private void adjustCaretPosition(int clickOffset) {
                int textLength = getText().length();
                int adjustedOffset = Math.min(Math.max(clickOffset, 0), textLength);
                setCaretPosition(adjustedOffset);
            }
        };
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
                drawstring(g, text, insets.left + alignmentOffset);
                g.setColor(getSelectedTextColor());
                drawstring(g, getSelectedText(), l);
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
                    drawstring(g, text, insets.left + alignmentOffset);
                }
            }
            if (hasFocus() && getSelectedText() == null) {
                if (!blink) {
                    g.setColor(getForeground());
                    int size = (int) (getFont().getSize()/2.6);
                    g.fillRect((size+4)*getCaretPosition() + alignmentOffset - 4, getHeight() / 2 - getFontMetrics(getFont()).getHeight() / 2, 2, getFontMetrics(getFont()).getHeight());
                }
            }
        } finally {
            g.dispose();
        }
    }
    private void drawstring(Graphics g, String text, int x) {
        int size = (int) (getFont().getSize()/2.6);
        int offset = 0;
        for (int i = 0; i < text.length(); i++) {
            g.fillOval(x + offset, getHeight()/2-size/2, size, size);
            offset += size + 4;
        }
    }
}