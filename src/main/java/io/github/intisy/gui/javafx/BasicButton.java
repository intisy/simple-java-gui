package io.github.intisy.gui.javafx;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.lang.Double;
import java.util.HashMap;

@SuppressWarnings("unused")
public class BasicButton extends ButtonBase {
    double height;
    double width;
    private Color textFillColor;
    private Color backgroundColor;
    private Color selectedBackgroundColor;
    private ExpandableString selected;
    private Font font;
    private final HashMap<ExpandableString, Label> labels = new HashMap<>();
    private final HashMap<ExpandableString, Rectangle> rectangles = new HashMap<>();
    private Double fontHeight;
    private ExpandableString texts;
    private Double fontWidth;
    private double xOffset = 27;
    public BasicButton(ExpandableString texts) {
        this(100, 30, texts);
    }
    public BasicButton(double width, double height, ExpandableString texts) {
        super(width, height);
        setHeight(height);
        setWidth(width);
        this.height = height;
        this.width = width;
        this.textFillColor = Colors.textColor;
        this.backgroundColor = Colors.backgroundColor;
        this.selectedBackgroundColor = Colors.selectedBackgroundColorBlue;
        this.rectangle.setFill(Color.TRANSPARENT);
        this.texts = texts;
        setCurrentTexts(texts);
    }

    public Rectangle getBoundingBox(Polygon polygon) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for (int i = 0; i < polygon.getPoints().size(); i += 2) {
            double x = polygon.getPoints().get(i);
            double y = polygon.getPoints().get(i + 1);

            if (x < minX) minX = x;
            if (y < minY) minY = y;
            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
        }

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    public void setCurrentTexts(ExpandableString texts) {
        double multiplier = height / 25;
        getChildren().clear();
        labels.clear();
        rectangles.clear();
        ExpandableString current = texts;
        int index = 0;
        double xOffset = this.xOffset;
        while (true) {
            Polygon arrow = new Polygon();
            arrow.getPoints().addAll(
                    xOffset - this.xOffset + 10.0*multiplier, height * index + height/2-2.5*multiplier,
                    xOffset - this.xOffset + 15.0*multiplier, height * index + height/2+2.5*multiplier,
                    xOffset - this.xOffset + 20.0*multiplier, height * index + height/2-2.5*multiplier,
                    xOffset - this.xOffset + 21.0*multiplier, height * index + height/2-1.5*multiplier,
                    xOffset - this.xOffset + 15.0*multiplier, height * index + height/2+4.5*multiplier,
                    xOffset - this.xOffset + 9.0*multiplier, height * index + height/2-1.5*multiplier
            );
            arrow.setFill(Color.rgb(147,163,170));
            Rectangle rectangle = new Rectangle(width, height);
            rectangle.setTranslateY(height * index);
            if (current == selected)
                rectangle.setFill(Colors.selectedBackgroundColorBlue);
            else
                rectangle.setFill(Colors.backgroundColor);
            int currentIndex = index;
            ExpandableString finalCurrent = current;
            javafx. event. EventHandler<? super javafx. scene. input. MouseEvent> value = event -> {
                if (selected != finalCurrent) {
                    select(finalCurrent);
                }
            };
            rectangle.setOnMouseClicked(value);
            String text = current.getValue();
            Label label = new Label(text);
            label.setOnMouseClicked(value);
            label.setTextFill(textFillColor);
            Font font = new Font(label.getFont().getFamily(), label.getFont().getSize()*(Math.min(height, width)/22));
            label.setFont(font);
            Text fontText = new Text(text);
            fontText.setFont(font);
            label.setLayoutY(height * index + (height - fontText.getBoundsInLocal().getHeight()) /2);
            label.setLayoutX(xOffset);
            labels.put(current, label);
            rectangles.put(current, rectangle);
            getChildren().addAll(rectangle, label);
            if (current.hasChildren()) {
                Rectangle arrowHitbox = getBoundingBox(arrow);
                if (!current.isExpanded()) {
                    arrow.setRotate(270);
                    arrowHitbox.setRotate(270);
                }
                arrowHitbox.setFill(Color.TRANSPARENT);
                arrowHitbox.setOnMouseClicked(event -> {
                    finalCurrent.setExpanded(!finalCurrent.isExpanded());
                    setCurrentTexts(texts);

                });
                getChildren().addAll(arrowHitbox, arrow);
            }
            if (current.isExpanded()) {
                current = current.subStrings.get(0);
                xOffset += 10;
            } else {
                ExpandableString parent = current.getParent();
                if (parent != null) {
                    int i = parent.subStrings.indexOf(current) + 1;
                    if (parent.subStrings.size() > i)
                        current = parent.subStrings.get(i);
                    else {
                        ExpandableString parent2 = parent.getParent();
                        if (parent2 != null) {
                            i = parent2.subStrings.indexOf(parent) + 1;
                            if (parent2.subStrings.size() > i) {
                                current = parent2.subStrings.get(i);
                                xOffset -= 10;
                            } else
                                break;
                        } else
                            break;
                    }
                } else
                    break;
            }
            index++;
        }
    }

    public void select(ExpandableString ExpandableString) {
        if (selected != null)
            rectangles.get(selected).setFill(backgroundColor);
        rectangles.get(ExpandableString).setFill(selectedBackgroundColor);
        selected = ExpandableString;
        callEvent();
    }

    public void setTexts(ExpandableString texts) {
        this.texts = texts;
    }
    public void setTextFill(Color color) {
        this.textFillColor = color;
    }
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        rectangles.forEach((ExpandableString, rectangle) -> {
            if (selected != ExpandableString)
                rectangles.get(ExpandableString).setFill(backgroundColor);
        });
    }
    private Interface onAction;

    public void setSelectedBackgroundColor(Color selectedBackgroundColor) {
        this.selectedBackgroundColor = selectedBackgroundColor;
        if (selected != null)
            rectangles.get(selected).setFill(backgroundColor);
    }

    public final void setXOffset(double xOffset) {
        this.xOffset = xOffset;
        setCurrentTexts(texts);
    }

    public final Font getFont() {
        return this.font;
    }

    public final void setFont(Font font) {
        labels.forEach((ExpandableString, label) -> {
            label.setFont(font);
            Text fontText = new Text(label.getText());
            fontText.setFont(font);
            label.setLayoutY((this.height - fontText.getBoundsInLocal().getHeight()) / 2);
            label.setLayoutX((this.width - fontText.getBoundsInLocal().getWidth()) / 2);
            this.font = font;
        });
    }

    public void callEvent() {
        onAction.execute(selected);
    }

    public final void setOnAction(Interface action) {
        this.onAction = action;
    }

    @FunctionalInterface
    public interface Interface {
        void execute(ExpandableString ExpandableString);
    }
}