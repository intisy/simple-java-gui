package io.github.intisy.gui.javafx;

import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

public class Timeline extends HoverPane {
    double height;
    double width;
    Rectangle background;
    MappedPane cursor;
    Color cursorColor = Color.WHITE;
    Color unvailiableCursorColor = Color.rgb((int) (cursorColor.getRed() * 150), (int) (cursorColor.getGreen() * 150), (int) (cursorColor.getBlue() * 150));
    Color backgroundColor = Color.BLACK;
    MediaPlayer mediaPlayer;

    public void setCursorX(double percent) {
        cursor.setLayoutX((width - cursor.getWidth()) * percent / 100);
    }

    public void setBackgroundColor(Color color) {
        backgroundColor = color;
        background.setFill(backgroundColor);
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        render();
    }

    public void setWidth(double width) {
        this.width = width;
        background.setWidth(width);
    }
    public void setHeight(double height) {
        this.height = height;
        background.setHeight(height);
    }
    public Timeline() {
        this(300, 300);
    }
    public Timeline(double width, double height) {
        this.width = width;
        this.height = height;
        render();
    }
    public void render() {
        boolean renderNew = background == null;
        boolean resize = background != null && (background.getWidth() != width || background.getHeight() != height);
        if (mediaPlayer != null && cursor != null) {
            int amountTillUpdate = 10;
            final int[] count = {0};
            Duration totalDuration = mediaPlayer.getTotalDuration();
            Path downPath = (Path) cursor.getChildrenMap().get("downPath");
            Path mainPath = (Path) cursor.getChildrenMap().get("mainPath");
            downPath.setStroke(cursorColor);
            mainPath.setStroke(cursorColor);
            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    setCursorX(getPlaybackPercentage(mediaPlayer));
                }
            };
            mediaPlayer.setOnPlaying(timer::start);
            mediaPlayer.setOnPaused(timer::stop);
            mediaPlayer.setOnStopped(timer::stop);
            mediaPlayer.setOnEndOfMedia(timer::stop);
            final double[] clickPoint = new double[1];
            cursor.setCursor(Cursor.W_RESIZE);
            cursor.setOnMousePressed(event -> clickPoint[0] = event.getX());
            cursor.setOnMouseDragged((event) -> {
                double x = (int) (cursor.getLayoutX() - clickPoint[0] + event.getX());
                if (x + cursor.getWidth() <= width && x >= 0) {
                    count[0]++;
                    if (count[0] == amountTillUpdate) {
                        Duration targetTime = totalDuration.multiply((cursor.getLayoutX() - clickPoint[0] + event.getX()) / (width - cursor.getWidth()));
                        mediaPlayer.seek(targetTime);
                        count[0] = 0;
                    }
                    cursor.setLayoutX(x);
                }
            });
            cursor.setOnMouseReleased(event -> {
                Duration targetTime = totalDuration.multiply((cursor.getLayoutX() - clickPoint[0] + event.getX()) / (width - cursor.getWidth()));
                mediaPlayer.seek(targetTime);
            });
        } else if (renderNew) {
            background = new Rectangle(width, height);
            background.setFill(backgroundColor);
            Path mainPath = new Path();
            mainPath.getElements().add(new MoveTo(2, 2));
            mainPath.getElements().add(new LineTo(6, 2));
            mainPath.getElements().add(new ArcTo(2, 2, 0, 8, 2, false, true));
            mainPath.getElements().add(new LineTo(8, 8));
            mainPath.getElements().add(new ArcTo(4, 4, 0, 0, 8, false, true));
            mainPath.getElements().add(new LineTo(0, 2));
            mainPath.getElements().add(new ArcTo(2, 2, 0, 2, 2, false, true));
            mainPath.getElements().add(new ClosePath());
            Path downPath = new Path();
            downPath.getElements().add(new MoveTo(4, 12));
            downPath.getElements().add(new LineTo(4, height));
            downPath.setStroke(unvailiableCursorColor);
            mainPath.setStroke(unvailiableCursorColor);
            mainPath.setFill(Color.TRANSPARENT);
            mainPath.setStrokeWidth(2);
            cursor = new MappedPane();
            cursor.getChildrenMap().putAll("mainPath", mainPath, "downPath", downPath);
            getChildren().addAll(background, cursor);
        } else if (resize) {
            double oldWidth = background.getWidth();
            assert cursor != null;
            background.setWidth(width);
            background.setHeight(height);
            Path downPath = (Path) cursor.getChildrenMap().get("downPath");
            for (PathElement element : downPath.getElements())
                if (element instanceof LineTo)
                    ((LineTo) element).setY(height);
            cursor.setLayoutX((width - cursor.getWidth()) / (oldWidth - cursor.getWidth()) * cursor.getLayoutX());
        }
    }

    public double getPlaybackPercentage(MediaPlayer mediaPlayer) {
        Duration currentTime = mediaPlayer.getCurrentTime();
        Duration totalDuration = mediaPlayer.getTotalDuration();
        if (totalDuration == null || totalDuration.isUnknown() || totalDuration.toMillis() == 0) {
            return 0;
        }
        return (currentTime.toMillis() / totalDuration.toMillis()) * 100;
    }
}
