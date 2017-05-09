package view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
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
import model.ImageCodec;
import utils.FileHelper;
import utils.Transition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ImageToImageController {

    @FXML
    private GridPane gridPane;

    @FXML
    private StackPane encodedStackPane;

    @FXML
    private StackPane decodedStackPane;

    @FXML
    private ImageView finalImageView;

    @FXML
    private ImageView decodedImageView;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXButton encodeButton;

    @FXML
    private StackPane stackPaneInput;

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
    private StackPane stackPane11;

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

    private ImageCodec imageCodec;

    private String imageInputPath;
    private String imageInfoPath;

    @FXML
    private void initialize() {
        imageCodec = new ImageCodec();
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

    private void imageViewAlignment(AnchorPane dottedPane, ImageView imageView, ImageView imageViewDrop, Rectangle rect){

        dottedPane.widthProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(imageView);
            Transition.LayoutImage(imageViewDrop);
            rect.setWidth((double)newV - 3);
        });

        dottedPane.heightProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(imageView);
            Transition.LayoutImage(imageViewDrop);
            rect.setHeight((double)newV - 3);
        });

        imageView.imageProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(imageView);
            Transition.LayoutImage(imageViewDrop);
            if (newV == null || imageViewDrop.getImage() == imageView.getImage()) {
                dottedPane.setStyle("-fx-border-style: segments(7); -fx-border-color: #869ff3");
                Transition.fadeOut(imageView);
                Transition.fadeIn(imageViewDrop);
            }
            else {
                dottedPane.setStyle("");
                Transition.fadeOut(imageViewDrop);
                Transition.fadeIn(imageView);
            }
        });

        Transition.LayoutImage(imageView);
        Transition.LayoutImage(imageViewDrop);
    }

    //EncodeTab

    @FXML
    void handleEncode(ActionEvent event) {
        Image image = imageCodec.encode1(imageInputPath, imageInfoPath, 1);
        finalImageView.setImage(image);
        saveButton.setVisible(true);
        showSnackBar("Изображение внедрено");
    }


    @FXML
    void handleDecode(ActionEvent event) {
        Image image = imageCodec.decode(imageViewToDecode.getImage());
        decodedImageView.setImage(image);
        saveButtonDecoded.setVisible(true);
        showSnackBar("Изображение извлечено");
        decodedImageView.setImage(imageViewToDecode.getImage());
    }

    @FXML
    void handleSaveFile(ActionEvent event) {

        File file;
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("File (*.png, *.bmp)", "*.png", "*.bmp");
        fileChooser.getExtensionFilters().addAll(extFilter);

        fileChooser.setTitle("Сохранить как");
        if((file = fileChooser.showSaveDialog(imageViewInfo.getScene().getWindow())) != null)  {
            String path = file.getPath();
            String outputFileExt = path.substring(path.lastIndexOf("."));
            //path = path.replace(outputFileExt, "*.bmp");
            File f = new File(path);
            outputFileExt = path.substring(path.lastIndexOf(".")+1);
            try {
                BufferedImage bImage = SwingFXUtils.fromFXImage(finalImageView.getImage(), null);
                ImageIO.write(bImage, outputFileExt.toUpperCase(), f);
            }
            catch(IOException e) {
                showSnackBar("Ошибка сохранения");
            }
        }
    }

    //ImageView as source

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

    @FXML
    void handleDropInput(DragEvent event) {
        try {
            List<File> files = event.getDragboard().getFiles();
            Image img = new Image(new FileInputStream(files.get(0)));
            String path = files.get(0).getPath();
            inputPathInput.setText(path);
            // FileExtention = path.substring(path.lastIndexOf("."));

            imageViewInput.setImage(img);
            closeButtonInput.setVisible(true);
            openButtonInput.setVisible(false);
            encodeButton.setDisable(false);

            openButtonInfo.setDisable(false);
            imageViewInfo.setDisable(false);
            imageViewDropInfo.setDisable(false);

            showSnackBar("Изображение добавлено");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleEnteredInput(DragEvent event) {
        Transition.fill(rectInput, Color.valueOf("#E0E0E0"), Color.WHITE);
        Transition.fadeOut(imageViewInput);
        Transition.fadeIn(imageViewDropInput);
        dottedPaneInput.setStyle("-fx-border-style: segments(7); -fx-border-color: #869ff3");
    }

    @FXML
    void handleExitedInput(DragEvent event) {
        Transition.fill(rectInput, Color.WHITE, Color.valueOf("#E0E0E0"));
        Transition.fadeIn(imageViewInput);
        Transition.fadeOut(imageViewDropInput);
        if (imageViewDropInput.getImage() != imageViewInput.getImage())
            dottedPaneInput.setStyle("");
    }

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

            imageInputPath = file.getPath();
            String path = file.getPath();
            inputPathInput.setText(path);
            // FileExtention = path.substring(path.lastIndexOf("."));

            Transition.fadeIn(imageViewInput);

            closeButtonInput.setVisible(true);
            openButtonInput.setVisible(false);
            encodeButton.setDisable(false);
            openButtonInfo.setDisable(false);

            showSnackBar("Изображение добавлено");

        }
    }

    //ImageView as information

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

            showSnackBar("Изображение добавлено");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleEnteredInfo(DragEvent event) {
        Transition.fill(rectInfo, Color.valueOf("#E0E0E0"), Color.WHITE);
        Transition.fadeOut(imageViewInfo);
        Transition.fadeIn(imageViewDropInfo);
        dottedPaneInfo.setStyle("-fx-border-style: segments(7); -fx-border-color: #869ff3");
    }

    @FXML
    void handleExitedInfo(DragEvent event) {
        Transition.fill(rectInfo, Color.WHITE, Color.valueOf("#E0E0E0"));
        Transition.fadeIn(imageViewInfo);
        Transition.fadeOut(imageViewDropInfo);
        if (imageViewDropInfo.getImage() != imageViewInfo.getImage())
            dottedPaneInfo.setStyle("");
    }

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

            imageInfoPath = file.getPath();
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

    @FXML
    void handleSaveFileDecoded(ActionEvent event) {

        File file;
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("File (*.png, *.bmp)", "*.png", "*.bmp");
        fileChooser.getExtensionFilters().addAll(extFilter);

        fileChooser.setTitle("Сохранить как");
        if((file = fileChooser.showSaveDialog(imageViewInfo.getScene().getWindow())) != null)  {
            String path = file.getPath();
            String outputFileExt = path.substring(path.lastIndexOf("."));
            //path = path.replace(outputFileExt, "*.bmp");
            File f = new File(path);
            outputFileExt = path.substring(path.lastIndexOf(".")+1);
            try {
                BufferedImage bImage = SwingFXUtils.fromFXImage(decodedImageView.getImage(), null);
                ImageIO.write(bImage, outputFileExt.toUpperCase(), f);
            }
            catch(IOException e) {
                showSnackBar("Ошибка сохранения");
            }
        }
    }

    @FXML
    void handleCloseToDecode(ActionEvent event) {
        if (imageViewToDecode.getImage() != null) {
            Transition.fadeOut(imageViewToDecode);
            Transition.fadeIn(imageViewDropToDecode);
            imageViewToDecode.setImage(imageViewDropToDecode.getImage());
            inputPathToDecode.setText("PATH");

            closeButtonToDecode.setVisible(false);
            openButtonToDecode.setVisible(true);
            saveButtonDecoded.setVisible(false);
            decodeButton.setDisable(true);
            decodedImageView.setImage(decodedImageViewAdd.getImage());
        }
    }

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

    @FXML
    void handleEnteredToDecode(DragEvent event) {
        Transition.fill(rectToDecode, Color.valueOf("#E0E0E0"), Color.WHITE);
        Transition.fadeOut(imageViewToDecode);
        Transition.fadeIn(imageViewDropToDecode);
        dottedPaneToDecode.setStyle("-fx-border-style: segments(7); -fx-border-color: #869ff3");
    }

    @FXML
    void handleExitedToDecode(DragEvent event) {
        Transition.fill(rectToDecode, Color.WHITE, Color.valueOf("#E0E0E0"));
        Transition.fadeIn(imageViewToDecode);
        Transition.fadeOut(imageViewDropToDecode);
        if (imageViewDropToDecode.getImage() != imageViewToDecode.getImage())
            dottedPaneToDecode.setStyle("");
    }

    @FXML
    void handleOpenFileToDecode(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        fileChooser.getExtensionFilters().
                addAll(new FileChooser.ExtensionFilter("Image Files (*.bmp, *.png, *.jpg, *.jpeg)",
                        "*.bmp", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(imageViewToDecode.getScene().getWindow());
        if (file != null) {
            String imageFile = file.toURI().toURL().toString();

            Image image = new Image(imageFile);
            imageViewToDecode.setImage(image);

            String path = file.getPath();
            inputPathToDecode.setText(path);
            // FileExtention = path.substring(path.lastIndexOf("."));

            Transition.fadeIn(imageViewToDecode);

            closeButtonToDecode.setVisible(true);
            openButtonToDecode.setVisible(false);
            decodeButton.setDisable(false);

            showSnackBar("Изображение добавлено");

        }
    }



    @FXML
    void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            List<File> files = event.getDragboard().getFiles();
            File file = files.get(0);

            if(FileHelper.checkExtension(file))
                event.acceptTransferModes(TransferMode.ANY);
        }
    }

    private void showSnackBar(String message) {
        snackBar = new JFXSnackbar(mainPane);
        snackBar.show(message, 2000);
    }

}
