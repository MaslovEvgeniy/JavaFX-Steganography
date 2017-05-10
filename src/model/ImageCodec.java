package model;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import utils.StegoCodec;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Maslov on 12.03.2017.
 */
public class ImageCodec implements StegoCodec<Image> {
    private BufferedImage currentImage;
    private BufferedImage hidImage;
    private byte data[];
    private byte dataToHide[];

    @Override
    public Image encode(Image image, Image message, int noOfLSB) {
        BufferedImage inImage = SwingFXUtils.fromFXImage(image, null);
        BufferedImage encImage = SwingFXUtils.fromFXImage(message, null);
        currentImage = transformImage(inImage);
        hidImage = transformImage(encImage);
        hideImage(1);
        Image img = getImageFromByteArray(data);
        return img;
        //return getImageFromByteArray(getImageData(transformImage(encImage)));
    }
    public Image encode1(String imagePath, String messagePath, int noOfLSB) {
        Path path = Paths.get(imagePath);
        Path pathHid = Paths.get(messagePath);
        try {
            data = Files.readAllBytes(path);
            dataToHide = Files.readAllBytes(pathHid);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        BufferedImage img1 = null;
//        Image img = null;
//        try {
//            img1 = ImageIO.read(new ByteArrayInputStream(data));
//            img = SwingFXUtils.toFXImage(img1,null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        hideImage(1);
        Image img = getImageFromByteArray(data);
        return img;
        //return getImageFromByteArray(getImageData(transformImage(encImage)));
    }

    @Override
    public Image decode(Image image) {
        return null;
    }

    private void hideImage(int noOfLSB) {
        //byte img[] = getImageData(currentImage);
        //byte msg[] = getImageData(hidImage);
//        byte img[] = data;
//        byte msg[] = dataToHide;
        byte msgLength[] = intToByteArray(dataToHide.length);
        hideLSB(data, noOfLSB);
        insertBytes(data, msgLength, 2, 1);
        insertBytes(data, dataToHide, 34, noOfLSB);
        //return img;
    }

    private void insertBytes(byte[] image, byte[] message, int offset, int noOfLSB) {
        for (int i = 0; i < message.length; ++i) {
            int add = message[i];
            for (int bit = 7; bit >= 0; --bit, ++offset) {
                int b = (add >>> bit) & 1;
                image[offset] = (byte) ((image[offset] & 0xFE) | b);
            }
        }
    }

    private void hideLSB(byte[] image, int noOfLSB) {
        int n = noOfLSB - 1;
        for (int i = 2; i >= 0; i--) {
            int b = (n >>> i) & 1;
            image[i] = (byte) ((image[i] & 0xFE) | b);
        }
    }

    private byte[] extractBytes(byte[] image) {
        int length = 0;
        int offset = 34;

        for (int i = 2; i < 34; ++i) {
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
    private byte[] getImageData(BufferedImage image) {
//        byte[] imageInByte = null;
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageIO.write(image, "jpg", baos);
//            baos.flush();
//            imageInByte = baos.toByteArray();
//            baos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return imageInByte;

        WritableRaster raster = image.getRaster();
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

    private Image getImageFromByteArray(byte[] bytes) {
        BufferedImage img1 = null;
        Image img = null;
//        try {
//            img1 = ImageIO.read(new ByteArrayInputStream(data));
//            img = SwingFXUtils.toFXImage(img1,null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return img;
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        try {
            img1 = ImageIO.read(bais);
            img = SwingFXUtils.toFXImage(img1,null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return img;
    }
}
