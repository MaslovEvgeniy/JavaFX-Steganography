package view;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import model.Controller;
import model.Model;
import model.TextDecoder;
import model.TextEncoder;
import utils.FileHelper;
import utils.Transition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class TextToImageController {


    @FXML
    private AnchorPane dottedPane;

    @FXML
    private GridPane gridPane;

    @FXML
    private JFXButton saveButton;

    @FXML
    private ImageView FinalImageView;

    @FXML
    private JFXTextArea resultText;

    @FXML
    private StackPane stackPane1;

    @FXML
    private StackPane encodedStackPane;

    @FXML
    private Rectangle rect1;

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView imageViewDrop;

    @FXML
    private JFXButton closeButton;

    @FXML
    private FontAwesomeIconView but_close;

    @FXML
    private JFXButton openButton;

    @FXML
    private FontAwesomeIconView but_open;

    @FXML
    private JFXTextField inputPath;

    @FXML
    private JFXTextArea textToEncode;

    @FXML
    private Label numbOfBitsText;

    @FXML
    private JFXSlider bitsSlider;

    @FXML
    private JFXButton encodeButton;

    @FXML
    private JFXButton DecodeButton;

    @FXML
    private JFXSnackbar snackBar;

    private static String FileExtention = null;

    private Controller controller = new Controller(makeModel());

    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            List<File> files = event.getDragboard().getFiles();
            File file = files.get(0);

            if(FileHelper.checkExtension(file))
                event.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    private void handleDrop(DragEvent event) {
        try {
            List<File> files = event.getDragboard().getFiles();
            Image img = new Image(new FileInputStream(files.get(0)));
            String path = files.get(0).getPath();
            inputPath.setText(path);
            FileExtention = path.substring(path.lastIndexOf("."));

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
    private void initialize() {
        imageView.setImage(imageViewDrop.getImage());
        dottedPane.widthProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(imageView);
            Transition.LayoutImage(imageViewDrop);
            rect1.setWidth((double)newV - 3);
        });

        dottedPane.heightProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(imageView);
            Transition.LayoutImage(imageViewDrop);
            rect1.setHeight((double)newV - 3);
        });

        encodedStackPane.widthProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(FinalImageView);
        });

        encodedStackPane.heightProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(FinalImageView);
        });

        FinalImageView.imageProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(FinalImageView);
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
        private void handleEntered(DragEvent event) {
        Transition.fill(rect1, Color.valueOf("#E0E0E0"), Color.WHITE);
        Transition.fadeOut(imageView);
        Transition.fadeIn(imageViewDrop);
        dottedPane.setStyle("-fx-border-style: segments(7); -fx-border-color: #869ff3");
    }

    @FXML
    private void handleExited(DragEvent event) {
        Transition.fill(rect1, Color.WHITE, Color.valueOf("#E0E0E0"));
        Transition.fadeIn(imageView);
        Transition.fadeOut(imageViewDrop);
        if (imageViewDrop.getImage() != imageView.getImage())
            dottedPane.setStyle("");
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
    private void handleOpenFile(ActionEvent event) throws IOException {

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
    private void handleSaveFile(ActionEvent event) {

        File file;
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("File (*.png, *.bmp)", "*.png", "*.bmp");
        fileChooser.getExtensionFilters().addAll(extFilter);

        fileChooser.setTitle("Save As");
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

    @FXML
    private void handleEncode(ActionEvent event) {
        controller.injectUI(imageView, FinalImageView, textToEncode, resultText);
        controller.onEncode();
        saveButton.setVisible(true);
        showSnackBar("Информация закодирована");
    }

    @FXML
    private void handleDecode(ActionEvent event) {
        controller.onDecode();
    }

    private Model makeModel() {
        return new Model(new TextEncoder(), new TextDecoder());
    }

    private void showSnackBar(String message) {
        snackBar = new JFXSnackbar(gridPane);
        snackBar.show(message, 2000);
    }
}
