package model;

import javafx.scene.image.Image;
import utils.StegoCodec;

/**
 * Created by Maslov on 12.03.2017.
 */
public class ImageCodec implements StegoCodec<Image> {
    @Override
    public Image encode(Image image, Image message) {
        return  null;
    }

    @Override
    public Image decode(Image image) {
        return null;
    }
}
