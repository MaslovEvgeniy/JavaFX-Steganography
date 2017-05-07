package view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextArea;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import model.Controller;
import model.Model;
import model.TextCodec;
import utils.FileHelper;
import utils.Transition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class TextToImageController {

    @FXML
    private GridPane gridPane;

    @FXML
    private StackPane encodedStackPane;

    @FXML
    private ImageView FinalImageView;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXTextArea resultText;

    @FXML
    private JFXButton encodeButton;

    @FXML
    private JFXButton DecodeButton;

    @FXML
    private StackPane stackPane1;

    @FXML
    private AnchorPane dottedPane;

    @FXML
    private Rectangle rect1;

    @FXML
    private ImageView imageView;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXButton openButton;

    @FXML
    private JFXButton decodeButton;

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
    private JFXTextArea decodedText;

    @FXML
    private JFXButton encodeButton1;

    @FXML
    private JFXSnackbar snackBar;

    private Controller controller = new Controller(makeModel());

    @FXML
    private void initialize() {
        imageView.setImage(imageViewDrop.getImage());
        imageViewToDecode.setImage(imageViewDropToDecode.getImage());

        encodedStackPane.widthProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(FinalImageView);
        });

        encodedStackPane.heightProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(FinalImageView);
        });

        FinalImageView.imageProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(FinalImageView);
        });

        imageViewAlignment(dottedPane, imageView, imageViewDrop, rect1);
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

    @FXML
    void handleDecode(ActionEvent event) {
       // controller.onDecode();
        showSnackBar("Текст извлечен");
        decodedText.setDisable(false);
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

    @FXML
    void handleDrop(DragEvent event) {
        try {
            List<File> files = event.getDragboard().getFiles();
            Image img = new Image(new FileInputStream(files.get(0)));
            String path = files.get(0).getPath();
            inputPath.setText(path);
            //FileExtention = path.substring(path.lastIndexOf("."));

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
    void handleEncode(ActionEvent event) {
        controller.injectUI(imageView, FinalImageView, textToEncode, resultText);
        controller.onEncode();
        saveButton.setVisible(true);
        showSnackBar("Информация закодирована");
    }

    @FXML
    void handleEntered(DragEvent event) {
        Transition.fill(rect1, Color.valueOf("#E0E0E0"), Color.WHITE);
        Transition.fadeOut(imageView);
        Transition.fadeIn(imageViewDrop);
        dottedPane.setStyle("-fx-border-style: segments(7); -fx-border-color: #869ff3");
    }

    @FXML
    void handleEnteredToDecode(DragEvent event) {
        Transition.fill(rectToDecode, Color.valueOf("#E0E0E0"), Color.WHITE);
        Transition.fadeOut(imageViewToDecode);
        Transition.fadeIn(imageViewDropToDecode);
        dottedPaneToDecode.setStyle("-fx-border-style: segments(7); -fx-border-color: #869ff3");
    }

    @FXML
    void handleExited(DragEvent event) {
        Transition.fill(rect1, Color.WHITE, Color.valueOf("#E0E0E0"));
        Transition.fadeIn(imageView);
        Transition.fadeOut(imageViewDrop);
        if (imageViewDrop.getImage() != imageView.getImage())
            dottedPane.setStyle("");
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
    void handleSaveFile(ActionEvent event) {
        File file;
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("File (*.png, *.bmp)", "*.png", "*.bmp");
        fileChooser.getExtensionFilters().addAll(extFilter);

        fileChooser.setTitle("Сохранить как");
        if((file = fileChooser.showSaveDialog(bitsSlider.getScene().getWindow())) != null)  {
            String path = file.getPath();
            String outputFileExt = path.substring(path.lastIndexOf("."));
            //path = path.replace(outputFileExt, "*.bmp");
            File f = new File(path);
            outputFileExt = path.substring(path.lastIndexOf(".")+1);
            try {
                BufferedImage bImage = SwingFXUtils.fromFXImage(FinalImageView.getImage(), null);
                ImageIO.write(bImage, outputFileExt.toUpperCase(), f);
            }
            catch(IOException e) {
                showSnackBar("Ошибка сохранения");
            }
        }
    }

    private Model makeModel() {
        return new Model(new TextCodec());
    }

    private void showSnackBar(String message) {
        snackBar = new JFXSnackbar(gridPane);
        snackBar.show(message, 2000);
    }
}
