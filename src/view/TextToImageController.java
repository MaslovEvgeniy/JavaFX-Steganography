package view;

import com.jfoenix.controls.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import model.StegoCodec;
import utils.FileHelper;
import utils.Transition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;


/**
 * Class controller for TextToImage.fxml
 */
public class TextToImageController {

    @FXML
    private StackPane encodedStackPane;

    @FXML
    private ImageView finalImageView;

    @FXML
    private ImageView finalImageViewAdd;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXButton encodeButton;

    @FXML
    private AnchorPane dottedPane;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Rectangle backgroundInputImage;

    @FXML
    private ImageView imageView;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXButton openButton;

    @FXML
    private JFXButton decodeButton;

    @FXML
    private JFXButton saveTextButton;

    @FXML
    private ImageView imageViewDrop;

    @FXML
    private JFXTextField inputPath;

    @FXML
    private JFXTextField inputPathToDecode;

    @FXML
    private JFXTextArea textToEncode;

    @FXML
    private AnchorPane dottedPaneToDecode;

    @FXML
    private Rectangle rectToDecode;

    @FXML
    private ImageView imageViewToDecode;

    @FXML
    private JFXButton closeButtonToDecode;

    @FXML
    private JFXButton openButtonToDecode;

    @FXML
    private ImageView imageViewDropToDecode;

    @FXML
    private JFXTextArea decodedText;

    @FXML
    private JFXSnackbar snackBar;

    private StegoCodec codec;

    private String resultText;

    private int maxInformSize = 0;

    /**
     * Sets image views alignment (Text-to-Image)
     */
    @FXML
    private void initialize() {

        codec = new StegoCodec();

        imageView.setImage(imageViewDrop.getImage());
        imageViewToDecode.setImage(imageViewDropToDecode.getImage());

        encodedStackPane.widthProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(finalImageView);
        });

        encodedStackPane.heightProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(finalImageView);
        });

        finalImageView.imageProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(finalImageView);
        });

        textToEncode.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textToEncode.getText().getBytes().length * 8 > maxInformSize) {
                // String s = textToEncode.getText().substring(0, maxInformSize-1);
                textToEncode.setText(oldValue);
                showSnackBar("Превышен максимальный размер информации, которую можно спрятать");
            }
        });

        imageViewAlignment(dottedPane, imageView, imageViewDrop, backgroundInputImage);
        imageViewAlignment(dottedPaneToDecode, imageViewToDecode, imageViewDropToDecode, rectToDecode);

    }

    /**
     * Sets image views alignment for transition
     *
     * @param dottedPane    dotted frame
     * @param imageView     main image
     * @param imageViewDrop background image
     * @param rect          background color for transition
     */
    private void imageViewAlignment(AnchorPane dottedPane, ImageView imageView, ImageView imageViewDrop, Rectangle rect) {

        dottedPane.widthProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(imageView);
            Transition.LayoutImage(imageViewDrop);
            rect.setWidth((double) newV - 3);
        });

        dottedPane.heightProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(imageView);
            Transition.LayoutImage(imageViewDrop);
            rect.setHeight((double) newV - 3);
        });

        imageView.imageProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(imageView);
            Transition.LayoutImage(imageViewDrop);
            if (newV == null || imageViewDrop.getImage() == imageView.getImage()) {
                dottedPane.setStyle("-fx-border-style: segments(7); -fx-border-color: #869ff3");
                Transition.fadeOut(imageView);
                Transition.fadeIn(imageViewDrop);
            } else {
                dottedPane.setStyle("");
                Transition.fadeOut(imageViewDrop);
                Transition.fadeIn(imageView);
            }
        });

        Transition.LayoutImage(imageView);
        Transition.LayoutImage(imageViewDrop);
    }

    //Encode tab

    /**
     * Closes input image for encoding
     *
     * @param event click
     */
    @FXML
    void handleClose(ActionEvent event) {
        if (imageView.getImage() != null) {
            Transition.fadeOut(imageView);
            Transition.fadeIn(imageViewDrop);
            imageView.setImage(imageViewDrop.getImage());
            inputPath.setText("PATH");

            closeButton.setVisible(false);
            openButton.setVisible(true);
            textToEncode.setDisable(true);
            encodeButton.setDisable(true);
        }
    }

    /**
     * Checks if the file can be dropped into the image view
     *
     * @param event drag event
     */
    @FXML
    void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            List<File> files = event.getDragboard().getFiles();
            File file = files.get(0);

            if (FileHelper.checkExtension(file))
                event.acceptTransferModes(TransferMode.ANY);
        }
    }

    /**
     * Assigns dragged image to the image view for encoding
     *
     * @param event drag event
     */
    @FXML
    void handleDrop(DragEvent event) {
        try {
            List<File> files = event.getDragboard().getFiles();
            Image img = new Image(new FileInputStream(files.get(0)));
            String path = files.get(0).getPath();
            inputPath.setText(path);

            imageView.setImage(img);
            closeButton.setVisible(true);
            openButton.setVisible(false);
            textToEncode.setDisable(false);
            encodeButton.setDisable(false);
            calcMaxInformationSize();

            showSnackBar("Изображение добавлено");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Action for encode button
     *
     * @param event click
     */
    @FXML
    void handleEncode(ActionEvent event) {
        if (textToEncode.getText().length() == 0) {
            showSnackBar("Введите сообщение для кодирования");
            return;
        }
        Image image = null;
        try {
            image = codec.encodeText(imageView.getImage(), textToEncode.getText());
        } catch (ArrayIndexOutOfBoundsException e) {
            showSnackBar("Невозможно закодировать данное сообщение");
            return;
        }

        finalImageView.setImage(image);
        saveButton.setVisible(true);
        showSnackBar("Информация закодирована");
    }

    /**
     * Sets transition if image is dragged into the image view for encoding
     *
     * @param event drag event
     */
    @FXML
    void handleEntered(DragEvent event) {
        Transition.fill(backgroundInputImage, Color.valueOf("#E0E0E0"), Color.WHITE);
        Transition.fadeOut(imageView);
        Transition.fadeIn(imageViewDrop);
        dottedPane.setStyle("-fx-border-style: segments(7); -fx-border-color: #869ff3");
    }

    /**
     * Sets transition if image is dropped into the image view for encoding
     *
     * @param event drag event
     */
    @FXML
    void handleExited(DragEvent event) {
        Transition.fill(backgroundInputImage, Color.WHITE, Color.valueOf("#E0E0E0"));
        Transition.fadeIn(imageView);
        Transition.fadeOut(imageViewDrop);
        if (imageViewDrop.getImage() != imageView.getImage())
            dottedPane.setStyle("");
    }

    /**
     * Opens input image for encoding
     *
     * @param event click
     * @throws IOException
     */
    @FXML
    void handleOpenFile(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        fileChooser.getExtensionFilters().
                addAll(new FileChooser.ExtensionFilter("Image Files (*.bmp, *.png, *.jpg, *.jpeg)",
                        "*.bmp", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(imageView.getScene().getWindow());
        if (file != null) {
            String imageFile = file.toURI().toURL().toString();

            Image image = new Image(imageFile);
            imageView.setImage(image);

            String path = file.getPath();
            inputPath.setText(path);
            // FileExtention = path.substring(path.lastIndexOf("."));

            Transition.fadeIn(imageView);

            closeButton.setVisible(true);
            openButton.setVisible(false);
            textToEncode.setDisable(false);
            encodeButton.setDisable(false);
            calcMaxInformationSize();

            showSnackBar("Изображение добавлено");

        }
    }

    /**
     * Saves the encoded image to file
     *
     * @param event click
     */
    @FXML
    void handleSaveFile(ActionEvent event) {
        File file;
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("File (*.png)", "*.png");
        fileChooser.getExtensionFilters().addAll(extFilter);

        fileChooser.setTitle("Сохранить как");
        if ((file = fileChooser.showSaveDialog(imageView.getScene().getWindow())) != null) {
            String path = file.getPath();
            String outputFileExt = path.substring(path.lastIndexOf("."));
            File f = new File(path);
            outputFileExt = path.substring(path.lastIndexOf(".") + 1);
            try {
                BufferedImage bImage = SwingFXUtils.fromFXImage(finalImageView.getImage(), null);
                ImageIO.write(bImage, outputFileExt.toUpperCase(), f);
                showSnackBar("Изображение сохранено в файл '" + file.getName() + "'");
            } catch (IOException e) {
                showSnackBar("Ошибка сохранения");
            }
        }
    }

    /**
     * Sets default settings for encode tab (Text-to-Image)
     *
     * @param event click
     */
    @FXML
    void handleRefreshEncode(ActionEvent event) {
        handleClose(event);
        textToEncode.clear();
        saveButton.setVisible(false);
        finalImageView.setImage(finalImageViewAdd.getImage());
    }

    //Decode tab

    /**
     * Action for decode button
     *
     * @param event click
     */
    @FXML
    void handleDecode(ActionEvent event) {
        try {
            resultText = codec.decodeText(imageViewToDecode.getImage());
        } catch (IOException e) {
            showSnackBar("Изображение не содержит закодированной информации");
            return;
        }
        decodedText.setText(resultText);
        showSnackBar("Текст извлечен");
        decodedText.setDisable(false);
        saveTextButton.setOpacity(1);
    }

    /**
     * Closes input image for decoding
     *
     * @param event click
     */
    @FXML
    void handleCloseToDecode(ActionEvent event) {
        if (imageViewToDecode.getImage() != null) {
            Transition.fadeOut(imageViewToDecode);
            Transition.fadeIn(imageViewDropToDecode);
            imageViewToDecode.setImage(imageViewDropToDecode.getImage());
            inputPathToDecode.setText("PATH");

            closeButtonToDecode.setVisible(false);
            openButtonToDecode.setVisible(true);
            decodeButton.setDisable(true);
        }
    }

    /**
     * Assigns dragged image to the image view for decoding
     *
     * @param event drag event
     */
    @FXML
    void handleDropToDecode(DragEvent event) {
        try {
            List<File> files = event.getDragboard().getFiles();
            Image img = new Image(new FileInputStream(files.get(0)));
            String path = files.get(0).getPath();
            inputPathToDecode.setText(path);

            imageViewToDecode.setImage(img);
            closeButtonToDecode.setVisible(true);
            openButtonToDecode.setVisible(false);
            decodeButton.setDisable(false);

            showSnackBar("Изображение добавлено");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set transition if image is dragged into the image view for decoding
     *
     * @param event drag event
     */
    @FXML
    void handleEnteredToDecode(DragEvent event) {
        Transition.fill(rectToDecode, Color.valueOf("#E0E0E0"), Color.WHITE);
        Transition.fadeOut(imageViewToDecode);
        Transition.fadeIn(imageViewDropToDecode);
        dottedPaneToDecode.setStyle("-fx-border-style: segments(7); -fx-border-color: #869ff3");
    }

    /**
     * Sets transition if image is dropped into the image view for decoding
     *
     * @param event drag event
     */
    @FXML
    void handleExitedToDecode(DragEvent event) {
        Transition.fill(rectToDecode, Color.WHITE, Color.valueOf("#E0E0E0"));
        Transition.fadeIn(imageViewToDecode);
        Transition.fadeOut(imageViewDropToDecode);
        if (imageViewDropToDecode.getImage() != imageViewToDecode.getImage())
            dottedPaneToDecode.setStyle("");
    }

    /**
     * Opens input image for decoding
     *
     * @param event click
     * @throws IOException
     */
    @FXML
    void handleOpenFileToDecode(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        fileChooser.getExtensionFilters().
                addAll(new FileChooser.ExtensionFilter("Image Files (*.bmp, *.png)",
                        "*.bmp", "*.png"));

        File file = fileChooser.showOpenDialog(imageViewToDecode.getScene().getWindow());
        if (file != null) {
            String imageFile = file.toURI().toURL().toString();

            Image image = new Image(imageFile);
            imageViewToDecode.setImage(image);

            String path = file.getPath();
            inputPathToDecode.setText(path);

            Transition.fadeIn(imageViewToDecode);

            closeButtonToDecode.setVisible(true);
            openButtonToDecode.setVisible(false);
            decodeButton.setDisable(false);

            showSnackBar("Изображение добавлено");

        }
    }

    /**
     * Saves the result text to file
     *
     * @param event click
     */
    @FXML
    void handleSaveTextFile(ActionEvent event) throws IOException { //TODO IGNORE ENTER!!!!!!!!!!
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(decodeButton.getScene().getWindow());

        if (file != null) {
            try (
                    BufferedReader reader = new BufferedReader(new StringReader(resultText));
                    PrintWriter writer = new PrintWriter(new FileWriter(file));
            ) {
                reader.lines().forEach(line -> writer.println(line));
            }
            showSnackBar("Текст сохранен в файл '" + file.getName() + "'");
        }


    }

    /**
     * Sets default settings for decode tab (Text-to-Image)
     *
     * @param event click
     */
    @FXML
    void handleRefreshDecode(ActionEvent event) {
        handleCloseToDecode(event);
        decodedText.clear();
        decodedText.setDisable(true);
        saveTextButton.setOpacity(0);
    }

    /**
     * Calculate max amount of information, that can be hidden
     */
    private void calcMaxInformationSize() {
        Image image = imageView.getImage();
        //3 - 3 channels(RGB), 35 = message length(32) + secretCode(3)
        maxInformSize = (int) image.getWidth() * (int) image.getHeight() * 3 - 35;
    }

    /**
     * Shows inform message
     *
     * @param message message to show
     */
    private void showSnackBar(String message) {
        snackBar = new JFXSnackbar(mainPane);
        snackBar.show(message, 2000);
    }
}