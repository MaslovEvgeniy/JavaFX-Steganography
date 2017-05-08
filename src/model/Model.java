package model;

import javafx.scene.image.Image;
import utils.StegoCodec;

public class Model {

    private StegoCodec codec;

    public Model(StegoCodec codec) {
        this.codec = codec;
    }

    public Image encode(Image image, String message, int noOfLSB) {
        return codec.encode(image, message, noOfLSB);
    }

    //decoder
    public String decode(Image image) {
        return (String) codec.decode(image);
    }
}