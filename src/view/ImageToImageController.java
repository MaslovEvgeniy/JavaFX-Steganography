package view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import model.StegoCodec;
import utils.FileHelper;
import utils.Transition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Class controller for ImageToImage.fxml
 */
public class ImageToImageController {

    @FXML
    private Label textExplain;

    @FXML
    private StackPane encodedStackPane;

    @FXML
    private StackPane decodedStackPane;

    @FXML
    private ImageView finalImageView;

    @FXML
    private ImageView finalImageViewAdd;

    @FXML
    private ImageView decodedImageView;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXButton encodeButton;

    @FXML
    private AnchorPane dottedPaneInput;

    @FXML
    private Rectangle rectInput;

    @FXML
    private ImageView imageViewInput;

    @FXML
    private JFXButton closeButtonInput;

    @FXML
    private JFXButton saveButtonDecoded;

    @FXML
    private JFXButton openButtonInput;

    @FXML
    private ImageView imageViewDropInput;

    @FXML
    private JFXTextField inputPathInput;

    @FXML
    private StackPane stackPaneInfo;

    @FXML
    private AnchorPane dottedPaneInfo;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Rectangle rectInfo;

    @FXML
    private ImageView imageViewInfo;

    @FXML
    private ImageView decodedImageViewAdd;

    @FXML
    private JFXButton closeButtonInfo;

    @FXML
    private JFXButton openButtonInfo;

    @FXML
    private ImageView imageViewDropInfo;

    @FXML
    private JFXTextField inputPathInfo;

    @FXML
    private JFXSnackbar snackBar;

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
    private JFXTextField inputPathToDecode;

    @FXML
    private JFXButton decodeButton;

    @FXML
    private JFXTextField maxSize;

    private StegoCodec codec;
    private double maxInformSize = 0.0;

    /**
     * Sets image views alignment (Image-to-Image)
     */
    @FXML
    private void initialize() {
        codec = new StegoCodec();
        imageViewInput.setImage(imageViewDropInput.getImage());
        imageViewToDecode.setImage(imageViewDropToDecode.getImage());

        encodedStackPane.widthProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(finalImageView);
        });

        encodedStackPane.heightProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(finalImageView);
        });

        decodedStackPane.heightProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(decodedImageView);
        });

        decodedStackPane.widthProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(decodedImageView);
        });

        finalImageView.imageProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(finalImageView);
        });

        decodedImageView.imageProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(decodedImageView);
        });


        imageViewAlignment(dottedPaneInput, imageViewInput, imageViewDropInput, rectInput);
        imageViewAlignment(dottedPaneInfo, imageViewInfo, imageViewDropInfo, rectInfo);
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

    //EncodeTab

    /**
     * Action for encode button
     *
     * @param event click
     */
    @FXML
    void handleEncode(ActionEvent event) {
        Image image = null;
        try {
            image = codec.encodeImage(imageViewInput.getImage(), inputPathInfo.getText());
        } catch (ArrayIndexOutOfBoundsException e) {
            showSnackBar("Невозможно закодировать данное изображение");
            return;
        }

        finalImageView.setImage(image);
        saveButton.setVisible(true);
        showSnackBar("Изображение внедрено");
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
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("File (*.png, *.bmp)", "*.png", "*.bmp");
        fileChooser.getExtensionFilters().addAll(extFilter);

        fileChooser.setTitle("Сохранить как");
        if ((file = fileChooser.showSaveDialog(imageViewInfo.getScene().getWindow())) != null) {
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

    //ImageView as source

    /**
     * Closes input image (as source) for encoding
     *
     * @param event click
     */
    @FXML
    void handleCloseInput(ActionEvent event) {
        if (imageViewInput.getImage() != null) {
            Transition.fadeOut(imageViewInput);
            Transition.fadeIn(imageViewDropInput);
            imageViewInput.setImage(imageViewDropInput.getImage());
            inputPathInput.setText("PATH");

            closeButtonInput.setVisible(false);
            openButtonInput.setVisible(true);
            encodeButton.setDisable(true);

        }
    }

    /**
     * Assigns dragged image to the image view (as source) for encoding
     *
     * @param event drag event
     */
    @FXML
    void handleDropInput(DragEvent event) {
        try {
            List<File> files = event.getDragboard().getFiles();
            Image img = new Image(new FileInputStream(files.get(0)));
            String path = files.get(0).getPath();
            inputPathInput.setText(path);
            // FileExtention = path.substring(path.lastIndexOf("."));

            if (!closeButtonInfo.isVisible())
                encodeButton.setDisable(true);
            else
                encodeButton.setDisable(false);

            imageViewInput.setImage(img);
            closeButtonInput.setVisible(true);
            openButtonInput.setVisible(false);

            openButtonInfo.setDisable(false);
            imageViewInfo.setDisable(false);
            imageViewDropInfo.setDisable(false);
            textExplain.setOpacity(1);
            stackPaneInfo.setOpacity(1);
            calcMaxInformationSize();

            showSnackBar("Изображение добавлено");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets transition if image is dragged into the image view (as source) for encoding
     *
     * @param event drag event
     */
    @FXML
    void handleEnteredInput(DragEvent event) {
        Transition.fill(rectInput, Color.valueOf("#E0E0E0"), Color.WHITE);
        Transition.fadeOut(imageViewInput);
        Transition.fadeIn(imageViewDropInput);
        dottedPaneInput.setStyle("-fx-border-style: segments(7); -fx-border-color: #869ff3");
    }

    /**
     * Sets transition if image is dropped into the image view (as source) for encoding
     *
     * @param event drag event
     */
    @FXML
    void handleExitedInput(DragEvent event) {
        Transition.fill(rectInput, Color.WHITE, Color.valueOf("#E0E0E0"));
        Transition.fadeIn(imageViewInput);
        Transition.fadeOut(imageViewDropInput);
        if (imageViewDropInput.getImage() != imageViewInput.getImage())
            dottedPaneInput.setStyle("");
    }

    /**
     * Opens input image (as source) for encoding
     *
     * @param event click
     * @throws IOException
     */
    @FXML
    void handleOpenFileInput(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        fileChooser.getExtensionFilters().
                addAll(new FileChooser.ExtensionFilter("Image Files (*.bmp, *.png, *.jpg, *.jpeg)",
                        "*.bmp", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(imageViewInput.getScene().getWindow());
        if (file != null) {
            String imageFile = file.toURI().toURL().toString();

            Image image = new Image(imageFile);
            imageViewInput.setImage(image);

            String path = file.getPath();
            inputPathInput.setText(path);
            // FileExtention = path.substring(path.lastIndexOf("."));

            Transition.fadeIn(imageViewInput);

            closeButtonInput.setVisible(true);
            openButtonInput.setVisible(false);
            if (imageViewInfo.getImage() != imageViewDropInfo.getImage())
                encodeButton.setDisable(false);
            openButtonInfo.setDisable(false);
            textExplain.setOpacity(1);
            stackPaneInfo.setOpacity(1);
            calcMaxInformationSize();

            showSnackBar("Изображение добавлено");

        }
    }

    //ImageView as information

    /**
     * Closes input image (as information) for encoding
     *
     * @param event click
     */
    @FXML
    void handleCloseInfo(ActionEvent event) {
        if (imageViewInfo.getImage() != null) {
            Transition.fadeOut(imageViewInfo);
            Transition.fadeIn(imageViewDropInfo);
            imageViewInfo.setImage(imageViewDropInfo.getImage());
            inputPathInfo.setText("PATH");

            closeButtonInfo.setVisible(false);
            openButtonInfo.setVisible(true);
            encodeButton.setDisable(true);
        }
    }

    /**
     * Assigns dragged image to the image view (as information) for encoding
     *
     * @param event drag event
     */
    @FXML
    void handleDropInfo(DragEvent event) {
        try {
            List<File> files = event.getDragboard().getFiles();
            Image img = new Image(new FileInputStream(files.get(0)));
            String path = files.get(0).getPath();
            inputPathInfo.setText(path);
            // FileExtention = path.substring(path.lastIndexOf("."));

            imageViewInfo.setImage(img);
            closeButtonInfo.setVisible(true);
            openButtonInfo.setVisible(false);
            encodeButton.setDisable(false);
            stackPaneInfo.setOpacity(1);

            showSnackBar("Изображение добавлено");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets transition if image is dragged into the image view (as information) for encoding
     *
     * @param event drag event
     */
    @FXML
    void handleEnteredInfo(DragEvent event) {
        Transition.fill(rectInfo, Color.valueOf("#E0E0E0"), Color.WHITE);
        Transition.fadeOut(imageViewInfo);
        Transition.fadeIn(imageViewDropInfo);
        dottedPaneInfo.setStyle("-fx-border-style: segments(7); -fx-border-color: #869ff3");
    }

    /**
     * Sets transition if image is dropped into the image view (as information) for encoding
     *
     * @param event drag event
     */
    @FXML
    void handleExitedInfo(DragEvent event) {
        Transition.fill(rectInfo, Color.WHITE, Color.valueOf("#E0E0E0"));
        Transition.fadeIn(imageViewInfo);
        Transition.fadeOut(imageViewDropInfo);
        if (imageViewDropInfo.getImage() != imageViewInfo.getImage())
            dottedPaneInfo.setStyle("");
    }

    /**
     * Opens input image (as information) for encoding
     *
     * @param event click
     * @throws IOException
     */
    @FXML
    void handleOpenFileInfo(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        fileChooser.getExtensionFilters().
                addAll(new FileChooser.ExtensionFilter("Image Files (*.bmp, *.png, *.jpg, *.jpeg)",
                        "*.bmp", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(imageViewInfo.getScene().getWindow());
        if (file != null) {
            String imageFile = file.toURI().toURL().toString();

            Image image = new Image(imageFile);
            imageViewInfo.setImage(image);

            //imageInfoPath = file.getPath();
            String path = file.getPath();
            inputPathInfo.setText(path);
            // FileExtention = path.substring(path.lastIndexOf("."));

            Transition.fadeIn(imageViewInfo);

            closeButtonInfo.setVisible(true);
            openButtonInfo.setVisible(false);
            encodeButton.setDisable(false);

            showSnackBar("Изображение добавлено");

        }
    }

    /**
     * Sets default settings for encode tab (Image-to-Image)
     *
     * @param event click
     */
    @FXML
    void handleRefreshEncode(ActionEvent event) {
        handleCloseInfo(event);
        handleCloseInput(event);

        stackPaneInfo.setOpacity(0.5);
        textExplain.setOpacity(0.5);
        saveButton.setVisible(false);
        finalImageView.setImage(finalImageViewAdd.getImage());
    }

    //Decode Tab

    /**
     * Action for decode button
     *
     * @param event click
     */
    @FXML
    void handleDecode(ActionEvent event) {
        Image image = null;
        try {
            image = codec.decodeImage(imageViewToDecode.getImage());
        } catch (IOException e) {
            showSnackBar("Изображение не содержит закодированной информации!");
            return;
        }
        decodedImageView.setImage(image);
        saveButtonDecoded.setVisible(true);
        showSnackBar("Изображение извлечено");
        decodedImageView.setImage(image);
    }

    /**
     * Saves the decoded image to file
     *
     * @param event click
     */
    @FXML
    void handleSaveFileDecoded(ActionEvent event) {

        File file;
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("File (*.png)", "*.png");
        fileChooser.getExtensionFilters().addAll(extFilter);

        fileChooser.setTitle("Сохранить как");
        if ((file = fileChooser.showSaveDialog(imageViewInfo.getScene().getWindow())) != null) {
            String path = file.getPath();
            String outputFileExt = path.substring(path.lastIndexOf("."));
            //path = path.replace(outputFileExt, "*.bmp");
            File f = new File(path);
            outputFileExt = path.substring(path.lastIndexOf(".") + 1);
            try {
                BufferedImage bImage = SwingFXUtils.fromFXImage(decodedImageView.getImage(), null);
                ImageIO.write(bImage, outputFileExt.toUpperCase(), f);
            } catch (IOException e) {
                showSnackBar("Ошибка сохранения");
            }
        }
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
     * Sets transition if image is dragged into the image view for decoding
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
     * Sets transition if image is dropped into the image view for coding
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
     * Sets default settings for decode tab (Image-to-Image)
     *
     * @param event click
     */
    @FXML
    void handleRefreshDecode(ActionEvent event) {
        handleCloseToDecode(event);

        saveButtonDecoded.setVisible(false);
        decodedImageView.setImage(decodedImageViewAdd.getImage());
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
     * Calculate max amount of information, that can be hidden
     */
    private void calcMaxInformationSize() {
        Image image = imageViewInput.getImage();
        //3 - 3 channels(RGB), 35 = message length(32) + secretCode(3)
        maxInformSize = ((int) image.getWidth() * (int) image.getHeight() * 3 - 35) / 8 / 1024;
        String res = String.format("%.1f", maxInformSize);
        maxSize.setText(res + " КБ");
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