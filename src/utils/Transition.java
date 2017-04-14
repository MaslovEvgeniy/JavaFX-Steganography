package utils;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class Transition {
    public static void fadeIn(Node node) {
        FadeTransition ftr = new FadeTransition(Duration.millis(500), node);
        ftr.setFromValue(0);
        ftr.setToValue(1);
        ftr.play();
    }
    public static void fadeOut(Node node) {
        FadeTransition ftr = new FadeTransition(Duration.millis(500), node);
        ftr.setFromValue(1);
        ftr.setToValue(0);
        ftr.play();
    }



    public static void fill(Shape shape, Color colorFrom, Color colorTo ) {
        FillTransition ft = new FillTransition(Duration.millis(500), shape, colorFrom, colorTo);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }
}