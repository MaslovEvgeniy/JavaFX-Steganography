package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import utils.StegoCodec;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

public class TextCodec implements StegoCodec<String> {

    private BufferedImage currentImage;

    @Override
    public Image encode(Image image, String message) {
        BufferedImage inImage = SwingFXUtils.fromFXImage(image, null);
        currentImage = transformImage(inImage);
        hideMessage(message);
        //System.out.println(decode());
        Image outputImage = SwingFXUtils.toFXImage(currentImage, null);
        currentImage = null;
        return outputImage;
    }

    @Override
    public String decode(Image image) {
        BufferedImage inImage = SwingFXUtils.fromFXImage(image, null);
        currentImage = transformImage(inImage);
        byte[] decode = extractBytes(getImageData());
        return (new String(decode));
    }

    private void hideMessage(String message) {
        byte img[] = getImageData();
        byte msg[] = message.getBytes();
        byte msgLength[] = intToByteArray(message.length());
        insertBytes(img, msgLength, 0);
        insertBytes(img, msg, 32);
    }

    private void insertBytes(byte[] image, byte[] message, int offset) {
        for (int i = 0; i < message.length; ++i) {
            int add = message[i];
            for (int bit = 7; bit >= 0; --bit, ++offset)
            {
                int b = (add >>> bit) & 1;
                image[offset] = (byte) ((image[offset] & 0xFE) | b);
            }
        }
    }

    private byte[] extractBytes(byte[] image) {
        int length = 0;
        int offset = 32;

        for (int i = 0; i < 32; ++i)
        {
            length = (length << 1) | (image[i] & 1);
        }

        byte[] result = new byte[length];

        for (int b = 0; b < result.length; ++b) {
            for (int i = 0; i < 8; ++i, ++offset) {
                result[b] = (byte) ((result[b] << 1) | (image[offset] & 1));
            }
        }
        return result;
    }


    //get image byte array
    private byte[] getImageData() {
        WritableRaster raster = currentImage.getRaster();
        DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
        return buffer.getData();
    }

    private BufferedImage transformImage(BufferedImage image)
    {
        BufferedImage newImg  = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = newImg.createGraphics();
        graphics.drawRenderedImage(image, null);
        graphics.dispose();

        return newImg;
    }

    private byte[] intToByteArray(int length) {
        byte[] result = new byte[4];

        result[0] = (byte) ((length & 0xFF000000) >> 24);
        result[1] = (byte) ((length & 0x00FF0000) >> 16);
        result[2] = (byte) ((length & 0x0000FF00) >> 8);
        result[3] = (byte) ((length & 0x000000FF));

        return result;
    }


}