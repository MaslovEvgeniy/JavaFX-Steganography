package model;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.util.Pair;
import utils.Encoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.stream.IntStream;

public class TextEncoder implements Encoder<String> {

    private static int noOfLSB =1;     //DELETE
    private static int x=0, y=0;

    @Override
    public Image encode(Image image, String message) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage copy = new WritableImage(image.getPixelReader(), width, height);
        PixelWriter writer = copy.getPixelWriter();
        PixelReader reader = image.getPixelReader();

        byte[] bits = mergeMLengthWithData(message);

        int[] pos = new int[]{0, 8, 16};

        int i = 128;

        int pixel = reader.getArgb(x, y);

        try {
            for(int b=0, len=bits.length; b<len; b++) {
                while(true) { //iterates for i
                    for(int j=0; j<pos.length; j++) { //iterates through positions
                        for(int k=pos[j]; k<pos[j]+noOfLSB; k++) { //iterates through no. of LSB's each position
                            if((bits[b] & i) != 0) { //(data[b] & i) gives bit in byte b
                                pixel = (pixel | (1 << k)); //change k^th(k= 1-> 8) bit in pixel to 1
                            }
                            else if((bits[b] & i) == 0) {
                                pixel = (pixel & ~(1 << k)); //change k^th(k= 1-> 8) bit in pixel to 0
                            }
                            if(i>0) i/=2;
                            if(i==0) {i=128;b++;} //gets new byte
                        }
                    }
                    if(b<bits.length) {
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

      /*  IntStream.range(0, bits.length)
                .mapToObj(i -> new Pair<>(i, reader.getArgb(i % width, i / width)))
                .map(pair -> new Pair<>(pair.getKey(), bits[pair.getKey()] ? pair.getValue() | 1 : pair.getValue() &~ 1))
                .forEach(pair -> {
                    int x = pair.getKey() % width;
                    int y = pair.getKey() / width;

                    writer.setArgb(x, y, pair.getValue());
                });
*/
        return copy;
    }


    private static byte[] mergeMLengthWithData(String message) {
        //merge message length with data
        //MLength uses 4 bytes to denote message size in bytes
        byte[] data = message.getBytes();
        ByteBuffer bf = ByteBuffer.allocate(4);
        bf.putInt(data.length+4); // add 4 with data.length to also include mLength size
        byte[] mLength = bf.array();
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bao.write(mLength);
            bao.write(data);
            data = bao.toByteArray();
        }
        catch(IOException e) {
           // AlertBox.error("Error in Encoding.", null);
        }
        return data;
    }


    private boolean[] encode(String message) {
        byte[] data = message.getBytes();

        // int = 32 bits
        // byte = 8 bits
        boolean[] bits = new boolean[32 + data.length * 8];

        // encode length
        String binary = Integer.toBinaryString(data.length);
        while (binary.length() < 32) {
            binary = "0" + binary;
        }

        for (int i = 0; i < 32; i++) {
            bits[i] = binary.charAt(i) == '1';
        }

        // [7, 6, 5 ... 0]
        // encode message
        for (int i = 0; i < data.length; i++) {
            byte b = data[i];

            for (int j = 0; j < 8; j++) {
                bits[32 + i*8 + j] = ((b >> (7 - j)) & 1) == 1;
            }
        }

        return bits;
    }


}