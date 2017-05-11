package utils;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/**
 * Class for transition and additional function
 */
public class Transition {
    /**
     * Transparency transition (object appearance)
     * @param node object
     */
    public static void fadeIn(Node node) {
        FadeTransition ftr = new FadeTransition(Duration.millis(500), node);
        ftr.setFromValue(0);
        ftr.setToValue(1);
        ftr.play();
    }

    /**
     * Transparency transition (object fading)
     * @param node object
     */
    public static void fadeOut(Node node) {
        FadeTransition ftr = new FadeTransition(Duration.millis(500), node);
        ftr.setFromValue(1);
        ftr.setToValue(0);
        ftr.play();
    }

    /**
     * Color transition for background figure
     * @param shape figure
     * @param colorFrom primary color
     * @param colorTo fina
     */
    public static void fill(Shape shape, Color colorFrom, Color colorTo ) {
        FillTransition ft = new FillTransition(Duration.millis(500), shape, colorFrom, colorTo);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    /**
     * Set image size to fit parent
     */
    public static void LayoutImage(ImageView imgV) {
        Pane parent = (Pane)imgV.getParent();
        if (imgV != null && imgV.getImage() != null) {
            int padding = 10;
            double reflectionKoeff = 0.0;
            double w = 0;
            double h = 0;
            double pw = parent.getWidth() - padding * 2;
            double ph = parent.getHeight() - padding * 2;

            Image img = imgV.getImage();
            double raitoX = pw / img.getWidth();
            double raitoY = ph / img.getHeight() / (1 + reflectionKoeff);

            double reduceKoeff = Math.min(Math.min(raitoY, raitoX), 1.0);// не
            // растягивать

            w = img.getWidth() * reduceKoeff;
            h = img.getHeight() * reduceKoeff;

            imgV.setFitWidth(w);
            imgV.setFitHeight(h);

            imgV.setLayoutX((pw - imgV.getFitWidth()) / 2 + padding);
            imgV.setLayoutY((ph - img.getHeight() * reduceKoeff * (1 + reflectionKoeff)) / 2 + padding);
        }
    }

    public static void LayoutImage(ImageView imgV, Region parent) {
        LayoutImage(imgV, parent.getWidth(), parent.getHeight());
    }

    public static void LayoutImage(ImageView imgV, double nw, double nh) {
        if (imgV != null && imgV.getImage() != null) {
            int padding = 10;
            double reflectionKoeff = 0.1;
            double w = 0;
            double h = 0;
            double pw = nw - padding * 2;
            double ph = nh - padding * 2;

            Image img = imgV.getImage();
            double raitoX = pw / img.getWidth();
            double raitoY = ph / img.getHeight() / (1 + reflectionKoeff);

            double reduceKoeff = Math.min(Math.min(raitoY, raitoX), 1.0);// не
            // растягивать

            w = img.getWidth() * reduceKoeff;
            h = img.getHeight() * reduceKoeff;

            imgV.setFitWidth(w);
            imgV.setFitHeight(h);

            imgV.setX((pw - imgV.getFitWidth()) / 2 + padding);
            imgV.setY((ph - img.getHeight() * reduceKoeff * (1 + reflectionKoeff)) / 2 + padding);
        }
    }
}