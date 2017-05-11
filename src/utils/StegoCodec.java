package utils;

import javafx.scene.image.Image;
/**
 * Interface for implementation of basic operations
 * @param <T> input information type
 */
public interface StegoCodec <T> {
   /**
    * //TODO
    * @param image
    * @param message
    * @param noOfLSB
    * @return
    */
   public Image encode(Image image, T message, int noOfLSB);

   /** //TODO
    *
    * @param image
    * @return
    */
   public T decode(Image image);
}
