package utils;

import javafx.scene.input.DragEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileHelper {
    public static Boolean checkExtension(File file) {
        List<String> validExtensions = new ArrayList<>();
        validExtensions.add("jpg");
        validExtensions.add("png");
        validExtensions.add("bmp");
        validExtensions.add("jpeg");

        String ext = getExtension(file.getName());

        return validExtensions.contains(ext);
    }

    private static String getExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return  "";
    }
}
