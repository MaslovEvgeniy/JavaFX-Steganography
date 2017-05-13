package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StegoCodec {

    private BufferedImage currentImage;
    private byte[] data;
    private int currentBit = 7;
    private int currentByte = 0;

    public Image encodeText(Image image, String message) throws ArrayIndexOutOfBoundsException {
        this.currentImage = SwingFXUtils.fromFXImage(image, null);
        byte[] msgBytes = null;
        try {
            msgBytes = message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (!checkSize(msgBytes.length))
            throw new ArrayIndexOutOfBoundsException();

        return encode(msgBytes);
    }

    public Image encodeImage(Image image, String hidImagePath) throws ArrayIndexOutOfBoundsException {
        this.currentImage = SwingFXUtils.fromFXImage(image, null);
        byte[] imgBytes = null;
        try {
            Path path = Paths.get(hidImagePath);
            imgBytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!checkSize(imgBytes.length))
            throw new ArrayIndexOutOfBoundsException();
        return encode(imgBytes);
    }

    public String decodeText(Image image) throws IOException {
        this.currentImage = SwingFXUtils.fromFXImage(image, null);
        if(!decode())
            throw new IOException();
        String result = new String(data);
        data = null;
        this.currentByte = 0;
        this.currentBit = 7;
        return result;
    }

    public Image decodeImage(Image image) throws IOException {
        this.currentImage = SwingFXUtils.fromFXImage(image, null);
        if(!decode())
            throw new IOException();;
        BufferedImage img = null;
        try {
            img = ImageIO.read(new ByteArrayInputStream(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image imageRes = SwingFXUtils.toFXImage(img, null);
        return imageRes;
    }

    private Image encode(byte[] msg) {
        byte[] data = new byte[4 + msg.length];
        byte[] msgLength = intToByteArray(msg.length);
        System.arraycopy(msgLength, 0, data, 0, msgLength.length);
        System.arraycopy(msg, 0, data, 4, msg.length);
        this.data = data;

        int w = currentImage.getWidth();
        int h = currentImage.getHeight();

        //set 000 into first pixel
        Color c = new Color(currentImage.getRGB(0, 0));
        int red = c.getRed() & 0xFE;
        int green = c.getGreen() & 0xFE;
        int blue = c.getBlue() & 0xFE;
        currentImage.setRGB(0, 0, new Color(red, green, blue, c.getAlpha()).getRGB());

        int i = 0, r = 0, g = 0, b = 0;
        Color color = null;
        try {
            for (i = 1; i < w * h; i++) {
                color = new Color(currentImage.getRGB(i % w, i / w));
                r = (color.getRed() & 0xFE | (generateNextBit()));
                g = (color.getGreen() & 0xFE | (generateNextBit()));
                b = (color.getBlue() & 0xFE | (generateNextBit()));
                currentImage.setRGB(i % w, i / w, new Color(r, g, b, color.getAlpha()).getRGB());
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            currentByte--;
            currentBit = 7;
            g = (color.getGreen() & 0xFE | (generateNextBit()));
            b = color.getBlue();
            currentImage.setRGB(i % w, i / w, new Color(r, g, b, color.getAlpha()).getRGB());

            Image image = SwingFXUtils.toFXImage(currentImage, null);
            return image;
        }
        return null;
    }

    private boolean decode() {
        this.data = null;
        this.currentByte = 0;
        this.currentBit = 7;

        int w = currentImage.getWidth();
        int h = currentImage.getHeight();

        Color c = new Color(currentImage.getRGB(0, 0));
        int lsb = 0;
        lsb = (c.getBlue() & 1) + ((c.getGreen() & 1) << 1) + ((c.getRed() & 1) << 2);


        int length = 0;
        this.data = new byte[4];
        int i = 1;
        try {
            for (i = 1; i < w * h; i++) {
                Color color = new Color(currentImage.getRGB(i % w, i / w));
                setNextBit(color.getRed() & 1);
                setNextBit(color.getGreen() & 1);
                setNextBit(color.getBlue() & 1);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            length = ByteBuffer.wrap(data).getInt();
            this.data = new byte[length];
            this.currentByte = 0;
            this.currentBit = 7;
            setNextBit(new Color(currentImage.getRGB(i % w, i / w)).getBlue() & 1);
        }

        if (lsb != 0 || (length <= 0 || length > Integer.MAX_VALUE - 4)) //check if we have encoded message
            return false;

        try {
            for (i = 12; i < w * h; i++) {
                Color color = new Color(currentImage.getRGB(i % w, i / w));
                setNextBit(color.getRed() & 1);
                setNextBit(color.getGreen() & 1);
                setNextBit(color.getBlue() & 1);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
        return false;
    }

    private boolean checkSize(int msgLength) {
        return (msgLength + 4) * 8 + 3 > currentImage.getWidth() * currentImage.getHeight() * 3;
    }

    private byte[] intToByteArray(int length) {
        byte[] result = new byte[4];

        result[0] = (byte) ((length & 0xFF000000) >> 24);
        result[1] = (byte) ((length & 0x00FF0000) >> 16);
        result[2] = (byte) ((length & 0x0000FF00) >> 8);
        result[3] = (byte) ((length & 0x000000FF));

        return result;
    }

    private int generateNextBit() throws ArrayIndexOutOfBoundsException {
        int r = (data[currentByte] >> currentBit) & 1;
        if (--currentBit < 0) {
            currentBit = 7;
            currentByte++;
        }
        return r;
    }

    private void setNextBit(int value) throws ArrayIndexOutOfBoundsException {
        data[currentByte] = (byte) (data[currentByte] | value << currentBit);
        if (--currentBit < 0) {
            currentByte++;
            currentBit = 7;
        }
    }


}