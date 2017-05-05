package model;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.util.Pair;
import utils.Encoder;

import javax.imageio.metadata.IIOMetadata;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.stream.IntStream;

public class TextEncoder implements Encoder<String> {

    private static int noOfLSBImage;
    private static int x=0, y=0;
    private static Image image;

    @Override
    public Image encode(Image inImage, String message, int _noOfLSB) {
        noOfLSBImage = _noOfLSB;
        image=inImage;

        WritableImage copy = new WritableImage(image.getPixelReader(), (int)image.getWidth(), (int)image.getHeight());

        writeBitsToImg(copy, prepareHeaderbits(message), 1 );
        writeBitsToImg(copy, prepareMessageBits(message), noOfLSBImage);

        return copy;
    }


    private void writeBitsToImg(WritableImage copy, boolean[] bits, int noOfLSB) {

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        PixelWriter writer = copy.getPixelWriter();
        PixelReader reader = image.getPixelReader();

        int[] pos = new int[]{0, 8, 16, 24};

        int i = 128;

        int pixel = reader.getArgb(x, y);

        try {
            for(int b=0, len=bits.length; b<len; b++) {
                while(true) { //iterates for i
                    for(int j = 0; j < pos.length; j++) { //iterates through positions
                        for(int k = pos[j]; k < pos[j] + noOfLSB; k++) { //iterates through no. of LSB's each position
                            if(bits[b]) { //(data[b] & i) gives bit in byte b
                                pixel = (pixel | (1 << k)); //change k^th(k= 1-> 8) bit in pixel to 1
                            }
                            else {
                                pixel = (pixel & ~(1 << k)); //change k^th(k= 1-> 8) bit in pixel to 0
                            }
                            if(i > 0) i /= 2;
                            if(i == 0) {i = 128; b++;} //gets new byte
                        }
                    }
                    if(b < bits.length) {
                        writer.setArgb(x++, y, pixel);

                        if(x < width && y < height) pixel = reader.getArgb(x, y);
                        else if(x >= width) {x=0; y++; pixel = reader.getArgb(x, y);}
                       // else if(y >= height) { AlertBox.error("Not enough pixels in Carrier image.", null); }
                    }
                    else throw new ArrayIndexOutOfBoundsException();
                }
            }
        }
        catch(ArrayIndexOutOfBoundsException e) {
            writer.setArgb(x++, y, pixel);
            //NOTE: Last pixel might be incompletely filled and hence x++, since next pixel only should be used for next set of insertion.
            if(x >= width) {x=0; y++;}
            //if(y >= height) { AlertBox.error("Not enough pixels in Carrier image.", null); } //Ran out of pixels
        }
    }

    private boolean[] prepareHeaderbits(String message) {

        boolean[] bits = new boolean[36]; //message length = 32 bits and noOfLSb = 4 bits

        // encode length
        String binary = Integer.toBinaryString(message.length());
        while (binary.length() < 32) {
            binary = "0" + binary;
        }

        for (int i = 0; i < 32; i++) {
            bits[i] = binary.charAt(i) == '1';
        }

        //encode noOfLSB size
        String bitsNumb = Integer.toBinaryString(noOfLSBImage);
        while (bitsNumb.length() < 4) {
            bitsNumb = "0" + bitsNumb;
        }

        for (int i = 32, j = 0; i < 36; i++, j++) {
                bits[i] = bitsNumb.charAt(j) == '1';
                System.out.println(bits[i]);//
        }

//        for (int i = 0; i < bits.length; i++) {
//            System.out.print(bits[i]);
//        }

        return bits;
    }

    private boolean[] prepareMessageBits(String message) {
        byte[] data = message.getBytes();
        boolean[] bits = new boolean[data.length * 8];

        for (int i = 0; i < data.length; i++) {
            byte b = data[i];

            for (int j = 0; j < 8; j++) {
                bits[i*8 + j] = ((b >> (7 - j)) & 1) == 1;
            }
        }

        return bits;
    }




}