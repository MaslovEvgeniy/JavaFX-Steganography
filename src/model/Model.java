package model;

import utils.Decoder;
import utils.Encoder;

public class Model {
    private Encoder encoder;
    private Decoder decoder;

    public Model(Encoder encoder, Decoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

}