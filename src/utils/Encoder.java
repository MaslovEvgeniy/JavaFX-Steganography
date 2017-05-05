package utils;

import javafx.scene.image.Image;

public interface Encoder <T> {
   public Image encode(Image image, T message, int noOfLSB);
}
