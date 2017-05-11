package utils;

import javafx.scene.input.DragEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for working with files
 */
public class FileHelper {
    /**
     * Check if file is image
     * @param file file
     * @return
     */
    public static Boolean checkExtension(File file) {
        List<String> validExtensions = new ArrayList<>();
        validExtensions.add("jpg");
        validExtensions.add("png");
        validExtensions.add("bmp");
        validExtensions.add("jpeg");

        String ext = getExtension(file.getName());

        return validExtensions.contains(ext);
    }

    /**
     * Get file extension
     * @param fileName file name
     * @return file extension
     */
    private static String getExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return  "";
    }
}
