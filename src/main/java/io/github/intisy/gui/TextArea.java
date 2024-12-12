package io.github.intisy.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.awt.event.MouseAdapter;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class TextArea extends Pane {
    double width;
    double height;
    private Color promptTextColor = Colors.darkTextColor;
    private Color textColor = Colors.textColor;
    private Color backgroundColor = Colors.lightBackgroundColor;
    private Color selectedStrokeColor = Colors.selectedStrokeColorBlue;
    private Color strokeColor = Colors.strokeColor;
    private final Rectangle rectangle;
    private final Label label;
    private final TextFlow textFlow;
    private final ScrollPane scrollPane;
    private boolean focused = false;
    private boolean selecting = false;
    private boolean centeredVertically;
    private int maxRows = -1;
    private int startSelectionIndex = -1;
    private int endSelectionIndex = -1;
    private final Rectangle caret;
    private Font font;
    private final javafx.animation.Timeline blinkTimeline;
    private int caretIndex = 0;
    private final Pane selectionPane = new Pane();

    public TextArea(double width, double height, double arc, ResizablePanel panel) {
        this(width, height, arc, panel, "");
    }
    public TextArea(double width, double height, double arc, ResizablePanel panel, String prompt) {
        this.width = width;
        this.height = height;
        rectangle = new Rectangle(width, height);
        rectangle.setFill(backgroundColor);
        rectangle.setArcWidth(arc);
        rectangle.setArcHeight(arc);
        label = new Label(prompt);
        label.setTextFill(promptTextColor);
        label.setPadding(new Insets(6));
        label.setWrapText(true);
        label.setMaxWidth(width);
        textFlow = new TextFlow();
        textFlow.setPadding(new Insets(5));
        textFlow.setStyle("-fx-background-color: transparent;");
        scrollPane = new ScrollPane(textFlow);
        scrollPane.setPrefWidth(width);
        scrollPane.setPrefHeight(height);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent");
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setFocusTraversable(false);
        caret = new Rectangle(1, 12, Color.WHITE);
        caret.setVisible(false);
        blinkTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), evt -> caret.setVisible(!caret.isVisible() && focused))
        );
        blinkTimeline.setCycleCount(Timeline.INDEFINITE);
        scrollPane.setOnMousePressed(this::startSelection);
        textFlow.setOnMouseDragged(this::updateSelection);
        scrollPane.setOnMouseReleased(this::endSelection);
        scrollPane.setOnKeyTyped(this::handleKeyTyped);
        scrollPane.setOnKeyPressed(this::handleKeyPressed);
        setFocusTraversable(true);
        setTextColor(textColor);
        setPromptTextColor(promptTextColor);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!isHover()) {
                    Platform.runLater(() -> hide());
                    blinkTimeline.pause();
                    caret.setVisible(false);
                    if (textFlow.getChildren().isEmpty())
                        label.setVisible(true);
                }
            }
        });
        setOnMouseClicked(event -> {
            scrollPane.requestFocus();
            focused = true;
            rectangle.setStroke(selectedStrokeColor);
            rectangle.setStrokeWidth(3);
            blinkTimeline.play();
            caret.setVisible(true);
            label.setVisible(false);
        });
        hide();
        getChildren().addAll(rectangle, selectionPane, scrollPane, caret, label);

        updateCaretPosition();
    }

    public void setFont(Font font) {
        for (Node text : textFlow.getChildren()) {
            if (text instanceof Text)
                ((Text) text).setFont(font);
        }
        this.font = font;
        caret.setHeight(font.getSize());
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public Font getFont() {
        return font != null ? font : ((Text) (textFlow.getChildren().get(0))).getFont();
    }

    private void startSelection(MouseEvent event) {
        selecting = true;
        startSelectionIndex = getCharacterIndexAt(event.getX(), event.getY());
        endSelectionIndex = startSelectionIndex;
        caretIndex = startSelectionIndex;
        updateCaretPosition();
        updateTextSelection();
    }

    private void updateSelection(MouseEvent event) {
        if (selecting) {
            endSelectionIndex = getCharacterIndexAt(event.getX(), event.getY());
            caretIndex = endSelectionIndex; // Update caret position during drag
            updateCaretPosition();
            updateTextSelection();
        }
    }

    public void setCenteredVertically(boolean centeredVertically) {
        this.centeredVertically = centeredVertically;
    }

    private void updateCaretPosition() {
        double xOff = 6;
        double yOff = -8;
        double xPos = 0;
        double yPos = 0;
        double defaultHeight = 0;
        int lastRow = 0;
        int row = 0;
        for (int i = 0; i < caretIndex; i++) {
            Text textNode = (Text) textFlow.getChildren().get(i);
            defaultHeight = textNode.getBoundsInLocal().getHeight();
            xPos += textNode.getBoundsInLocal().getWidth();
            if (textNode.getText().contains("\n"))
                row++;
            else {
                while (row * defaultHeight != textNode.getLayoutY() - 5) {
                    row++;
                }
            }
            if (lastRow != row) {
                lastRow = row;
                yPos = defaultHeight * (row + 1);
                if (textNode.getText().contains("\n"))
                    xPos = 0;
                else
                    xPos = textNode.getBoundsInLocal().getWidth();
            }
        }
        if (yPos != 0)
            yPos += yOff;
        else
            yPos = defaultHeight + yOff;
        caret.setTranslateX(xOff + xPos);
        caret.setTranslateY(yPos);
    }

    private void endSelection(MouseEvent event) {
        if (selecting) {
            endSelectionIndex = getCharacterIndexAt(event.getX(), event.getY());
            updateTextSelection();
            selecting = false;
        }
    }

    private int getCharacterIndexAt(double x, double y) {
        int size = 0;
        int index = 0;
        Map<java.lang.Double, java.lang.Double> widths = new HashMap<>();
        for (Node node : textFlow.getChildren()) {
            Text textNode = (Text) node;
            double nodeWidth = textNode.getBoundsInLocal().getWidth();
            widths.put(textNode.getLayoutY(), widths.getOrDefault(textNode.getLayoutY(), 5D) + nodeWidth);
            if (widths.get(textNode.getLayoutY()) >= x) {
                if (textNode.getLayoutY() + textNode.getBoundsInLocal().getHeight() >= y && y > textNode.getLayoutY())
                    return index;
            }
            if (textNode.getLayoutY() < y || widths.size() == 1)
                size++;
            index += textNode.getText().length();
        }
        return size;
    }

    private void updateTextSelection() {
        if (startSelectionIndex == -1 || endSelectionIndex == -1) {
            return;
        }
        int minIndex = Math.min(startSelectionIndex, endSelectionIndex);
        int maxIndex = Math.max(startSelectionIndex, endSelectionIndex);
        for (Node node : textFlow.getChildren()) {
            ((Text) node).setFill(textColor);
        }
        int currentIndex = 0;
        selectionPane.getChildren().clear();
        for (Node node : textFlow.getChildren()) {
            Text textNode = (Text) node;
            int textLength = textNode.getText().length();
            if (currentIndex >= minIndex && currentIndex + textLength <= maxIndex) {
                Rectangle background = new Rectangle(textNode.getBoundsInLocal().getWidth(), textNode.getBoundsInLocal().getHeight());
                background.setLayoutX(textNode.getLayoutX());
                background.setLayoutY(textNode.getLayoutY());
                background.setFill(Color.rgb(9, 69, 179, 0.8));
                selectionPane.getChildren().add(background);
            } else {
                textNode.setFill(textColor);
            }
            currentIndex += textLength;
        }
    }

    private void handleKeyTyped(KeyEvent event) {
        if (!event.isControlDown() && focused) {
            String character = event.getCharacter();
            if (character.equals("\r")) {
                if (getRows() != maxRows)
                    replaceSelectedText("\n");
            } else if (!character.equals("\b")) {
                replaceSelectedText(character);
            }
            updateCaretPosition();
        }
    }

    public int getRows() {
        int rows = 1;
        for (Node node : textFlow.getChildren()) {
            if (node instanceof Text)
                if (((Text) node).getText().equals("\n"))
                    rows++;
        }
        return rows;
    }

    private void handleKeyPressed(KeyEvent event) {
        if (focused) {
            if (event.isControlDown()) {
                if (event.getCode() == KeyCode.BACK_SPACE) {
                    if (hasSelection()) {
                        replaceSelectedText("");
                    } else {
                        boolean first = true;
                        while ((!textFlow.getChildren().isEmpty() && caretIndex > 0 && !((Text) textFlow.getChildren().get(caretIndex - 1)).getText().equals(" ")) || first) {
                            textFlow.getChildren().remove(caretIndex - 1);
                            caretIndex--;
                            first = false;
                        }
                    }
                } else if (event.getCode() == KeyCode.C) {
                    copySelectedText();
                } else if (event.getCode() == KeyCode.X) {
                    cutSelectedText();
                } else if (event.getCode() == KeyCode.V) {
                    pasteClipboardContent();
                } else if (event.getCode() == KeyCode.A) {
                    selectAllText();
                }
            } else if (event.getCode() == KeyCode.BACK_SPACE) {
                if (hasSelection()) {
                    replaceSelectedText("");
                } else if (!textFlow.getChildren().isEmpty() && caretIndex > 0) {
                    textFlow.getChildren().remove(caretIndex - 1);
                    caretIndex--;
                }
            } else if (event.getCode() == KeyCode.LEFT) {
                moveCaretLeft();
            } else if (event.getCode() == KeyCode.RIGHT) {
                moveCaretRight();
            } else {
                return;
            }
            updateCaretPosition();
        }
    }

    private void moveCaretLeft() {
        if (caretIndex > 0) {
            caretIndex--;
        }
    }

    private void moveCaretRight() {
        if (caretIndex < textFlow.getChildren().size()) {
            caretIndex++;
        }
    }

    private void selectAllText() {
        if (textFlow.getChildren().isEmpty()) return;
        startSelectionIndex = 0;
        endSelectionIndex = textFlow.getChildren().size();
        caretIndex = endSelectionIndex;
        updateCaretPosition();
        updateTextSelection();
    }

    private void replaceSelectedText(String replacement) {
        Text text = new Text(replacement);
        text.setFill(textColor);
        if (font != null)
            text.setFont(font);
        if (hasSelection()) {
            selectionPane.getChildren().clear();
            int minIndex = Math.min(startSelectionIndex, endSelectionIndex);
            int maxIndex = Math.max(startSelectionIndex, endSelectionIndex);
            textFlow.getChildren().subList(minIndex, maxIndex).clear();
            textFlow.getChildren().add(minIndex, text);
            caretIndex = minIndex + replacement.length();
        } else {
            textFlow.getChildren().add(caretIndex, text);
            caretIndex++;
        }
        clearSelection();
    }

    public String getText() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Node node : textFlow.getChildren()) {
            stringBuilder.append(((Text) node).getText());
        }
        return stringBuilder.toString();
    }

    public void setText(String text) {
        textFlow.getChildren().clear();
        for (int i = 0; i < text.length(); i++) {
            String s = text.substring(i, i + 1);
            Text node = new Text(s);
            node.setFill(textColor);
            if (font != null)
                node.setFont(font);
            textFlow.getChildren().add(caretIndex, node);
            caretIndex++;
        }
        if (!textFlow.getChildren().isEmpty())
            label.setVisible(false);
    }

    private void copySelectedText() {
        if (hasSelection()) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(getSelectedText());
            clipboard.setContent(content);
        }
    }

    private void cutSelectedText() {
        if (hasSelection()) {
            copySelectedText();
            replaceSelectedText(""); // Remove selected text
        }
    }

    private void pasteClipboardContent() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            String pasteContent = clipboard.getString();
            for (int i = 0; i < pasteContent.length(); i++) {
                String s = pasteContent.substring(i, i + 1);
                replaceSelectedText(s);
            }
        }
    }

    private String getSelectedText() {
        if (!hasSelection()) return "";
        int minIndex = Math.min(startSelectionIndex, endSelectionIndex);
        int maxIndex = Math.max(startSelectionIndex, endSelectionIndex);
        StringBuilder selectedText = new StringBuilder();
        for (int i = minIndex; i < maxIndex; i++) {
            Text textNode = (Text) textFlow.getChildren().get(i);
            selectedText.append(textNode.getText());
        }
        return selectedText.toString();
    }

    private boolean hasSelection() {
        return startSelectionIndex != endSelectionIndex;
    }

    private void clearSelection() {
        startSelectionIndex = -1;
        endSelectionIndex = -1;
        updateTextSelection();
    }

    public void hide() {
        focused = false;
        rectangle.setStroke(strokeColor);
        rectangle.setStrokeWidth(1);
    }

    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        rectangle.setFill(backgroundColor);
    }

    public Color getPromptTextColor() {
        return promptTextColor;
    }

    public void setPromptTextColor(Color promptTextColor) {
        this.promptTextColor = promptTextColor;
    }

    public Color getSelectedStrokeColor() {
        return selectedStrokeColor;
    }

    public void setSelectedStrokeColor(Color selectedStrokeColor) {
        this.selectedStrokeColor = selectedStrokeColor;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
        rectangle.setStroke(strokeColor);
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public double getCurrentHeight() {
        return height;
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
    }
}
