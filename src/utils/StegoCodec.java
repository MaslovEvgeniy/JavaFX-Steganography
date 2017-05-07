package utils;

import javafx.scene.image.Image;

public interface StegoCodec <T> {
   public Image encode(Image image, T message);
   public T decode(Image image);
}
