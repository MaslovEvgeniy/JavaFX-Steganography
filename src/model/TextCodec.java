package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import utils.StegoCodec;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class TextCodec implements StegoCodec<String> {

    private BufferedImage currentImage;

    @Override
    public Image encode(Image image, String message, int noOfLSB) {
        BufferedImage inImage = SwingFXUtils.fromFXImage(image, null);
        currentImage = transformImage(inImage);
        try {
            hideMessage(message, noOfLSB);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
        String result = null;
        try {
            result = new String(decode, detectEncoding(decode));
            System.out.println(detectEncoding(decode));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void hideMessage(String message, int noOfLSB) throws UnsupportedEncodingException {
        byte img[] = getImageData();
        System.out.println(detectEncoding(message.getBytes()));
        byte msg[] = message.getBytes("UTF-8");
        byte msgLength[] = intToByteArray(msg.length);
        insertBytes(img, msgLength, 0, 1);
        insertBytes(img, msg, 32, noOfLSB);
    }

    private void insertBytes(byte[] image, byte[] message, int offset, int noOfLSB) {
        for (int i = 0; i < message.length; ++i) {
            int add = message[i];
            for (int bit = 7; bit >= 0; --bit, ++offset)
            {
                int b = (add >>> bit) & 1;
                image[offset] = (byte) ((image[offset] & 0xFE) | b);
            }
        }
//        for (int i = 0; i < message.length; i++) {
//            int add = message[i];
//            int[] arr = new int[8];
//            for (int b = 7; b >= 0; b++) {
//                arr[b] = (add >>> b) & 1;
//            }
//            for (int n = 1, b = 7; n < noOfLSB; n++, ++offset) {
//                image[offset] = (byte) ((image[offset] & ((0xFE << n - 1) - 1) | (arr[b--] << n)));
//                if (b == 0) break;
//            }
//            int b=7, n=1;
//            while(b!=0) {
//                if(n==1) {
//                    image[offset] = (byte) ((image[offset] & 0xFE | arr[b--]));
//                }
//                b--;
//            }
//        }
    }

    private byte[] extractBytes(byte[] image) {
        int length = 0;
        int offset = 32;

        for (int i = 0; i < 32; ++i) {
            length = (length << 1) | (image[i] & 1);
        }

        byte[] result = new byte[length];

        for (int b = 0; b < result.length; ++b) {
            for (int i = 0; i < 8; ++i, ++offset) {
                result[b] = (byte) ((result[b] << 1) | (image[offset] & 1));
            }
        }
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + " ");
        }
        return result;
    }


    //get image byte array
    private byte[] getImageData() {
        WritableRaster raster = currentImage.getRaster();
        DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
        return buffer.getData();
    }

    private BufferedImage transformImage(BufferedImage image) {
        BufferedImage newImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
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

    private String detectEncoding(byte[] in) throws UnsupportedEncodingException {
        CharsetDetector detector = new CharsetDetector().setText(in);
        CharsetMatch charsetMatch = detector.detect();
        if (charsetMatch == null) {
            return "UTF-8";
        }
        return charsetMatch.getName();
    }

}