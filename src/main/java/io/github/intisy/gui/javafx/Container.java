package io.github.intisy.gui.javafx;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Container extends MappedPane {
    public static final int TOP=1, RIGHT=2, BOTTOM=3, LEFT=4;
    Color outlineColor = Color.rgb(30,31,34);
    Color color = Color.rgb(43,45,48);
    double width;
    double height;
    public boolean resizable = false;
    boolean outlineTop = false, outlineBottom = false, outlineRight = false, outlineLeft = false;
    double outlineWidth = 1;
    double outlineHitboxSize = 5;
    public Rectangle bg1, bg2, outlineTopHitbox, outlineRightHitbox;
    double x;
    double y;
    public List<Object> onResize = new ArrayList<>();
    public Container setResizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }

    public void addLineBreak(String name, double y, double sizeMultiplier) {
        addLineBreak(name, y, 16 * sizeMultiplier, sizeMultiplier);
    }

    public Pane addLineBreak(String name, double y, double x, double sizeMultiplier) {
        Pane pane = new Pane();
        pane.setLayoutY(y);
        pane.setLayoutX(x);
        Label label = new Label(name);
        Font font = new Font(label.getFont().getFamily(), label.getFont().getSize()*sizeMultiplier);
        label.setFont(font);
        label.setTextFill(Colors.textColor);
        Rectangle rectangle = new Rectangle(0, 0, 0, 0.5);
        label.widthProperty().addListener((observable, oldValue, newValue) -> {
            rectangle.setX(newValue.doubleValue() + 9 * sizeMultiplier);
            rectangle.setWidth(width - newValue.doubleValue() - x - 39 * sizeMultiplier);
        });
        onResize.add((SizeInterface) (width, height) -> rectangle.setWidth(width - label.getWidth() - x - 39 * sizeMultiplier));
        label.heightProperty().addListener((observable, oldValue, newValue) -> rectangle.setY(newValue.doubleValue()/2 + 1));
        rectangle.setFill(Color.rgb(65,65,73));
        pane.getChildren().addAll(rectangle, label);
        getChildren().add(pane);
        return pane;
    }

    public Container(double width, double height) {
        setMaxWidth(width);
        setMinWidth(width);
        setCurrentWidth(width);
        setMaxHeight(height);
        setMinHeight(height);
        setCurrentHeight(height);
        drawContainer();
    }

    public boolean setCurrentHeight(double height) {
        return setCurrentHeight(height, false);
    }
    public boolean setCurrentHeight(double height, boolean ignore) {
        if (((height >= getMinHeight() || getMinHeight() == -1) && ((height <= getMaxHeight()) || getMaxHeight() == -1)) || ignore) {
            setHeight((float) height);
            drawContainer();
            return true;
        }
        return false;
    }

    public double getCurrentHeight() {
        return height;
    }

    public double getCurrentWidth() {
        return width;
    }

    public boolean setCurrentWidth(double width) {
        return setCurrentWidth(width, false);
    }
    public boolean setCurrentWidth(double width, boolean ignore) {
        if (((width >= getMinWidth() || getMinWidth() == -1) && ((width <= getMaxWidth()) || getMaxWidth() == -1)) || ignore) {
            setWidth((float) width);
            drawContainer();
            return true;
        }
        return false;
    }
    public Container setWidth(float width) {
        double oldWidth = this.width;
        this.width = width;
        super.setWidth(width);
        if (outlineRightHitbox != null)
            outlineRightHitbox.setX(outlineRightHitbox.getX() + (width - oldWidth));
        if (outlineTopHitbox != null)
            outlineTopHitbox.setWidth(width);
        return this;
    }
    public Container setHeight(float height) {
        double oldHeight = this.height;
        this.height = height;
        super.setWidth(height);
        if (outlineRightHitbox != null)
            outlineRightHitbox.setHeight(height);
        return this;
    }
    public Container setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
        drawContainer();
        return this;
    }

    public Container setColor(Color color) {
        this.color = color;
        drawContainer();
        return this;
    }
    public List<Container> getRelatedTreeY(MappedParent children) {
        return getRelatedTreeY(children, false, new ArrayList<>());
    }
    public List<Container> getRelatedTreeY(MappedParent children, boolean inverted, List<Container> containers) {
        containers.add(this);
        children.getChildrenMap().forEach((name, child) -> {
            if (child instanceof Container) {
                if (hasRelatedY((Container) child, inverted)) {
                    if (!containers.contains(child)) {
                        ((Container) child).getRelatedTreeY(children, !inverted, containers).forEach(c -> {
                            if (!containers.contains(c)) {
                                containers.add(c);
                            }
                        });
                    }
                }
            }
        });
        return containers;
    }
    public List<Container> getRelatedTreeX(MappedParent children) {
        return getRelatedTreeX(children, false, new ArrayList<>());
    }
    public List<Container> getRelatedTreeX(MappedParent children, boolean inverted, List<Container> containers) {
        containers.add(this);
        children.getChildrenMap().forEach((name, child) -> {
            if (child instanceof Container) {
                if (hasRelatedX((Container) child, inverted)) {
                    if (!containers.contains(child)) {
                        ((Container) child).getRelatedTreeX(children, !inverted, containers).forEach(c -> {
                            if (!containers.contains(c)) {
                                containers.add(c);
                            }
                        });
                    }
                }
            }
        });
        return containers;
    }
    public boolean isBetween(double key1, double key2, double value) {
        return key1 <= value && key2 >= value;
    }
    public boolean hasRelatedY(Container container) {
        return hasRelatedY(container, false);
    }
    public boolean hasRelatedY(Container container, boolean inverted) {
        if (getY() + (inverted ? getCurrentHeight() : 0) == container.getY() + (inverted ? 0 : container.getCurrentHeight())) {
            return isBetween(getX(), getX() + getCurrentWidth(), container.getX()) ||
                    isBetween(getX(), getX() + getCurrentWidth(), container.getX() + container.getCurrentWidth()) ||
                    isBetween(container.getX(), container.getX() + container.getCurrentWidth(), getX()) ||
                    isBetween(container.getX(), container.getX() + container.getCurrentWidth(), getX() + getCurrentWidth());
        }
        return false;
    }
    public boolean hasRelatedX(Container container) {
        return hasRelatedX(container, false);
    }
    public boolean hasRelatedX(Container container, boolean inverted) {
        if (getX() + (inverted ? 0 : getCurrentWidth()) == container.getX() + (inverted ? container.getCurrentWidth() : 0)) {
            return isBetween(getY(), getY() + getCurrentHeight(), container.getY()) ||
                    isBetween(getY(), getY() + getCurrentHeight(), container.getY() + container.getCurrentHeight()) ||
                    isBetween(container.getY(), container.getY() + container.getCurrentHeight(), getY()) ||
                    isBetween(container.getY(), container.getY() + container.getCurrentHeight(), getY() + getCurrentHeight());
        }
        return false;
    }
    public void addOnResize(SizeInterface action) {
        onResize.add(action);
    }
    public void callOnResize(double width, double height) {
        for (Object action : onResize) {
            if (action instanceof SizeInterface) {
                ((SizeInterface) action).execute(width, height);
            } else if (action instanceof ContainerInterface) {
                ((ContainerInterface) action).execute(this);
            }
        }
    }
    public void callOnResize() {
        callOnResize(width, height);
    }
    public Container setOutline(int side, boolean outline, String name, MappedParent children, boolean resizable) {
        switch (side) {
            case TOP:
                if (resizable && this.resizable) {
                    if (outline) {
                        if (children.get(name + ".hitbox.top") == null) {
                            outlineTopHitbox = new Rectangle(width, outlineHitboxSize);
                            outlineTopHitbox.setLayoutX(x);
                            outlineTopHitbox.setLayoutY(y - Math.floor(outlineHitboxSize / 2));
                            outlineTopHitbox.setCursor(Cursor.N_RESIZE);
                            outlineTopHitbox.setFill(Color.TRANSPARENT);
                            outlineTopHitbox.setStroke(Color.TRANSPARENT);
                            final Point[] clickPoint = new Point[1];
                            outlineTopHitbox.setOnMousePressed(event -> clickPoint[0] = new Point((int) event.getX(), (int) event.getY()));
                            outlineTopHitbox.setOnMouseDragged(event -> {
                                int yOffset = (int) (clickPoint[0].y - event.getY());
                                clickPoint[0] = new Point((int) event.getX(), (int) event.getY());
                                List<Container> relatedChildren = getRelatedTreeY(children);
                                int stop = -1;
                                int index = 0;
                                for (Container child : relatedChildren) {
                                    if (child == this) {
                                        if (!child.setCurrentHeight(child.getCurrentHeight() + yOffset)) {
                                            stop=index;
                                            break;
                                        } else
                                            child.setY(child.getY() - yOffset);
                                    } else {
                                        if (!child.setCurrentHeight(child.getCurrentHeight() - yOffset)) {
                                            stop=index;
                                            break;
                                        }
                                    }
                                    index++;
                                }
                                index = 0;
                                if (stop != -1) {
                                    for (Container child : relatedChildren) {
                                        if (index == stop)
                                            break;
                                        if (child == this) {
                                            relatedChildren.get(index).setCurrentHeight(child.getCurrentHeight() - yOffset);
                                            child.setY(child.getY() + yOffset);
                                        } else
                                            relatedChildren.get(index).setCurrentHeight(child.getCurrentHeight() + yOffset);
                                        index++;
                                    }
                                }
                            });
                            children.add(name + ".hitbox.top", outlineTopHitbox);
                        }
                    } else
                        children.remove(name + ".hitbox.top");
                    outlineTopHitbox.toFront();
                }
                this.outlineTop = outline;
                break;
            case RIGHT:
                if (resizable && this.resizable) {
                    if (outline) {
                        if (children.get(name + ".hitbox.right") == null) {
                            outlineRightHitbox = new Rectangle(outlineHitboxSize, height);
                            outlineRightHitbox.setLayoutX(x + width - Math.floor(outlineHitboxSize / 2));
                            outlineRightHitbox.setLayoutY(y);
                            outlineRightHitbox.setCursor(Cursor.E_RESIZE);
                            outlineRightHitbox.setFill(Color.TRANSPARENT);
                            outlineRightHitbox.setStroke(Color.TRANSPARENT);
                            final Point[] clickPoint = new Point[1];
                            outlineRightHitbox.setOnMousePressed(event -> clickPoint[0] = new Point((int) event.getX(), (int) event.getY()));
                            outlineRightHitbox.setOnMouseDragged(event -> {
                                int xOffset = (int) (clickPoint[0].x - event.getX());
                                clickPoint[0] = new Point((int) event.getX(), (int) event.getX());
                                List<Container> relatedChildren = getRelatedTreeX(children);
                                int stop = -1;
                                int index = 0;
                                double originalX = getX();
                                double originalWidth = getWidth();
                                for (Container child : relatedChildren) {
                                    if (child.getX() + child.getWidth() == originalX + originalWidth) {
                                        if (!child.setCurrentWidth(child.getCurrentWidth() - xOffset)) {
                                            stop=index;
                                            break;
                                        }
                                    } else {
                                        if (!child.setCurrentWidth(child.getCurrentWidth() + xOffset)) {
                                            stop=index;
                                            break;
                                        } else
                                            child.setX(child.getX() - xOffset);
                                    }
                                    index++;
                                }
                                index = 0;
                                originalX = getX();
                                originalWidth = getWidth();
                                if (stop != -1) {
                                    for (Container child : relatedChildren) {
                                        if (index == stop)
                                            break;
                                        if (child.getX() + child.getWidth() == originalX + originalWidth) {
                                            relatedChildren.get(index).setCurrentWidth(child.getCurrentWidth() + xOffset);
                                        } else {
                                            relatedChildren.get(index).setCurrentWidth(child.getCurrentWidth() - xOffset);
                                            child.setX(child.getX() + xOffset);
                                        }
                                        index++;
                                    }
                                }
                            });
                            children.add(name + ".hitbox.right", outlineRightHitbox);
                        }
                    } else
                        children.remove(name + ".hitbox.right");
                    outlineRightHitbox.toFront();
                }
                this.outlineRight = outline;
                break;
            case BOTTOM: // not needed
                this.outlineBottom = outline;
                break;
            case LEFT:
                this.outlineLeft = outline;
                break;
        }
        drawContainer();
        return this;
    }

    public boolean getOutline(int side) {
        switch (side) {
            case TOP:
                return outlineTop;
            case RIGHT:
                return outlineRight;
            case BOTTOM:
                return outlineBottom;
            case LEFT:
                return outlineLeft;
            default:
                return false;
        }
    }

    public Container setOutlineX(double x) {
        if (outlineTopHitbox != null)
            outlineTopHitbox.setLayoutX(x);
        if (outlineRightHitbox != null)
            outlineRightHitbox.setLayoutX(x - outlineHitboxSize);
        return this;
    }
    public Container setOutlineY(double y) {
        if (outlineTopHitbox != null)
            outlineTopHitbox.setLayoutY(y - outlineHitboxSize);
        if (outlineRightHitbox != null)
            outlineRightHitbox.setLayoutY(y);
        return this;
    }
    public Container setX(double x) {
        if (outlineRightHitbox != null) {
            outlineRightHitbox.setX(outlineRightHitbox.getX() + (x - this.x));
        }
        this.x = x;
        drawContainer();
        return this;
    }
    public Container addX(double x) {
        return setX(this.x + x);
    }

    public double getX() {
        return x;
    }

    public Container setY(double y) {
        if (outlineTopHitbox != null) {
            outlineTopHitbox.setY(outlineTopHitbox.getY() - (this.y - y));
        }
        this.y = y;
        drawContainer();
        return this;
    }

    public Container addY(double y) {
        return setY(this.y + y);
    }

    public double getY() {
        return y;
    }

    public double getOutlineWidth() {
        return outlineWidth;
    }

    private void drawContainer() {
        callOnResize(width, height);
        int offsetX = 0;
        int offsetY = 0;
        int offsetWidth = 0;
        int offsetHeight = 0;
        super.setLayoutX(x);
        super.setLayoutY(y);
        bg1 = new Rectangle(width, height);
        bg1.setFill(outlineColor);
        if (outlineTop) {
            offsetY++;
            offsetHeight--;
        }
        if (outlineRight) {
            offsetWidth--;
        }
        if (outlineBottom) {
            offsetHeight--;
        }
        if (outlineLeft) {
            offsetX++;
            offsetWidth--;
        }
        bg2 = new Rectangle(width+offsetWidth, height+offsetHeight);
        bg2.setLayoutX(offsetX);
        bg2.setLayoutY(offsetY);
        bg2.setFill(color);
        if (getChildren().isEmpty())
            getChildren().addAll(bg1, bg2);
        else {
            getChildren().set(0, bg1);
            getChildren().set(1, bg2);
        }
    }
    public void clear() {
        getChildren().clear();
        onResize.clear();
        drawContainer();
    }

    @FunctionalInterface
    public interface SizeInterface {
        void execute(double width, double height);
    }

    @FunctionalInterface
    public interface ContainerInterface {
        void execute(Container container);
    }
}
