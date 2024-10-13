package io.github.intisy.gui.javafx;

import javafx.animation.KeyFrame;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.awt.event.MouseAdapter;
import java.util.HashMap;
import java.util.Map;

public class TextArea extends Pane {
    double width;
    double height;
    private Color promptTextColor = Colors.darkTextColor;
    private Color textColor = Colors.textColor;
    private Color backgroundColor = Colors.lightBackgroundColor;
    private Color selectedStrokeColor = Colors.selectedStrokeColorBlue;
    private Color strokeColor = Colors.strokeColor;
    private final Rectangle rectangle;
    private Label label;
    private TextFlow textFlow;
    private ScrollPane scrollPane;
    private boolean focused = false;

    private boolean selecting = false;
    private int startSelectionIndex = -1;
    private int endSelectionIndex = -1;

    private Rectangle caret;
    private javafx.animation.Timeline blinkTimeline;
    private int caretIndex = 0;

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
        caret = new Rectangle(1, 13, Color.WHITE);
        caret.setVisible(false);
        blinkTimeline = new javafx.animation.Timeline(
                new KeyFrame(Duration.seconds(0.5), evt -> caret.setVisible(!caret.isVisible() && focused))
        );
        blinkTimeline.setCycleCount(javafx.animation.Timeline.INDEFINITE);
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
        getChildren().addAll(rectangle, scrollPane, caret, label);

        updateCaretPosition();
    }

    private void startSelection(MouseEvent event) {
        selecting = true;
        startSelectionIndex = getCharacterIndexAt(event.getX(), event.getY());
        endSelectionIndex = startSelectionIndex;
        caretIndex = startSelectionIndex; // Update caret position
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

    private void updateCaretPosition() {
        double xOff = 7;
        double yOff = -8;
        double xPos = 0;
        double yPos = 0;
        double defaultHeight = 15.9609375;
        int lastRow = 0;
        int row = 0;
        for (int i = 0; i < caretIndex; i++) {
            Text textNode = (Text) textFlow.getChildren().get(i);
            xPos += textNode.getBoundsInLocal().getWidth();
            if (textNode.getText().contains("\n"))
                row++;
            else
                while (row * defaultHeight != textNode.getLayoutY() - 5) {
                    row++;
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
        for (Node node : textFlow.getChildren()) {
            Text textNode = (Text) node;
            int textLength = textNode.getText().length();

            if (currentIndex >= minIndex && currentIndex + textLength <= maxIndex) {
                textNode.setFill(Color.BLUE); // Highlight with a different color
            } else {
                textNode.setFill(textColor); // Default text color
            }

            currentIndex += textLength;
        }
    }

    private void handleKeyTyped(KeyEvent event) {
        if (!event.isControlDown() && focused) {
            String character = event.getCharacter();
            if (character.equals("\r")) {
                replaceSelectedText("\n");
            } else if (!character.equals("\b")) {
                replaceSelectedText(character);
            }
            updateCaretPosition();
        }
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
        if (hasSelection()) {
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
//        textField.setStyle("-fx-text-fill: " + toRGBCode(textColor) + "; " +
//                "-fx-prompt-text-fill: " + toRGBCode(promptTextColor) + ";");
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
//        textField.setStyle("-fx-text-fill: " + toRGBCode(textColor) + "; " +
//                "-fx-prompt-text-fill: " + toRGBCode(promptTextColor) + ";");
    }

    public double getCurrentHeight() {
        return height;
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
    }
}
