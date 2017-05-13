package view;

import com.jfoenix.controls.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
import model.TextCodec;
import utils.FileHelper;
import utils.Transition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.logging.Logger;

public class TextToImageController {

    @FXML
    private JFXTabPane tabPane;

    @FXML
    private GridPane gridPane;

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
    private StackPane paneForImage;

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
    private Label numbOfBitsText;

    @FXML
    private JFXSlider bitsSlider;

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
    private JFXButton refreshButtonEncode;

    @FXML
    private JFXButton refreshButtonDecode;

    @FXML
    private ImageView imageViewDropToDecode;

    @FXML
    private JFXTextArea decodedText;

    @FXML
    private JFXSnackbar snackBar;

    private TextCodec textCodec;
    private String resultText;

    /**
     * Set image views alignment (Text-to-Image)
     */
    @FXML
    private void initialize() {

        textCodec = new TextCodec();

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

        imageViewAlignment(dottedPane, imageView, imageViewDrop, backgroundInputImage);
        imageViewAlignment(dottedPaneToDecode, imageViewToDecode, imageViewDropToDecode, rectToDecode);

    }

    /**
     * Set image views alignment for transition
     * @param dottedPane dotted frame
     * @param imageView main image
     * @param imageViewDrop background image
     * @param rect background color for transition
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
     * Close input image for encoding
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
            bitsSlider.setDisable(true);
            numbOfBitsText.setOpacity(0.5);
            encodeButton.setDisable(true);
        }
    }

    /**
     * Check if the file can be dropped into the image view
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
     * Assign dragged image to the image view for encoding
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
            bitsSlider.setDisable(false);
            numbOfBitsText.setOpacity(1);
            encodeButton.setDisable(false);

            showSnackBar("Изображение добавлено");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Action for encode button
     * @param event click
     */
    @FXML
    void handleEncode(ActionEvent event) { //TODO ADD
        Image image = textCodec.encode(imageView.getImage(), textToEncode.getText(), 1);
        finalImageView.setImage(image);
        saveButton.setVisible(true);
        showSnackBar("Информация закодирована");
    }

    /**
     * Set transition if image is dragged into the image view for encoding
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
     * Set transition if image is dropped into the image view for encoding
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
     * Open input image for encoding
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
            bitsSlider.setDisable(false);
            numbOfBitsText.setOpacity(1);
            encodeButton.setDisable(false);

            showSnackBar("Изображение добавлено");

        }
    }

    /**
     * Save the encoded image to file
     * @param event click
     */
    @FXML
    void handleSaveFile(ActionEvent event) {
        File file;
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("File (*.png, *.bmp)", "*.png", "*.bmp");
        fileChooser.getExtensionFilters().addAll(extFilter);

        fileChooser.setTitle("Сохранить как");
        if ((file = fileChooser.showSaveDialog(bitsSlider.getScene().getWindow())) != null) {
            String path = file.getPath();
            String outputFileExt = path.substring(path.lastIndexOf("."));
            //path = path.replace(outputFileExt, "*.bmp");
            File f = new File(path);
            outputFileExt = path.substring(path.lastIndexOf(".") + 1);
            try {
                BufferedImage bImage = SwingFXUtils.fromFXImage(finalImageView.getImage(), null);
                ImageIO.write(bImage, outputFileExt.toUpperCase(), f);
            } catch (IOException e) {
                showSnackBar("Ошибка сохранения");
            }
        }
    }

    /**
     * Set default settings for encode tab (Text-to-Image)
     * @param event click
     */
    @FXML
    void handleRefreshEncode(ActionEvent event) {
        handleClose(event);
        textToEncode.clear();
        bitsSlider.setValue(1.0);
        saveButton.setVisible(false);
        finalImageView.setImage(finalImageViewAdd.getImage());
    }

    //Decode tab

    /**
     * Action for decode button
     * @param event click
     */
    @FXML
    void handleDecode(ActionEvent event) {
            resultText = textCodec.decode(imageViewToDecode.getImage());
            decodedText.setText(resultText);
            showSnackBar("Текст извлечен");
            decodedText.setDisable(false);
            saveTextButton.setOpacity(1);
    }

    /**
     * Closes input image for decoding
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
     * Set transition if image is dropped into the image view for decoding
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
     * Open input image for decoding
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
     * Save the result text to file
     * @param event click
     */
    @FXML
    void handleSaveTextFile(ActionEvent event) throws IOException{
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(decodeButton.getScene().getWindow());

        if(file != null){
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
     * Set default settings for decode tab (Text-to-Image)
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
     * Show inform message
     * @param message message to show
     */
    private void showSnackBar(String message) {
        snackBar = new JFXSnackbar(mainPane);
        snackBar.show(message, 2000);
    }
}