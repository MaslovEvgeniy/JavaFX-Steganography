package view;


import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import utils.*;
import utils.Transition;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWindowController implements Initializable {

    @FXML
    private JFXTextArea textToEncode;

    @FXML
    private JFXTextArea resultText;

    @FXML
    private JFXButton encodeButton;

    @FXML
    private JFXButton DecodeButton;

    @FXML
    private JFXButton openButton;

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView imageViewDrag;

    @FXML
    private ImageView FinalImageView;

    @FXML
    private JFXTextField inputPath;

    @FXML
    private Rectangle rect1;

    private static String FileExtention = null;

    private Controller controller = new Controller(makeModel());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            VBox box = FXMLLoader.load(getClass().getResource("DrawerContent.fxml"));
            drawer.setSidePane(box);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HamburgerBasicCloseTransition close = new HamburgerBasicCloseTransition(hamburger);
        close.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            close.setRate(close.getRate() * -1);
            close.play();
            if (drawer.isShown()) {
                drawer.close();
            } else {
                drawer.open();
            }
        });
    }

    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            List <File> files = event.getDragboard().getFiles();
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
          } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEntered(DragEvent event) {
        Transition.fill(rect1, Color.valueOf("#E0E0E0"), Color.WHITE);
        Transition.fadeOut(imageView);
        Transition.fadeIn(imageViewDrag);
    }

    @FXML
    private void handleExited(DragEvent event) {
        Transition.fill(rect1, Color.WHITE, Color.valueOf("#E0E0E0"));
        Transition.fadeIn(imageView);
        Transition.fadeOut(imageViewDrag);
    }

    @FXML
    void handleClose(ActionEvent event) {
        if (imageView.getImage() != null) {
            Transition.fadeOut(imageView);
            imageView.setImage(null);
            inputPath.setText("PATH");

            Transition.fadeIn(imageViewDrag);

            closeButton.setVisible(false);
            openButton.setVisible(true);
            textToEncode.setDisable(true);
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
            FileExtention = path.substring(path.lastIndexOf("."));

            Transition.fadeOut(imageViewDrag);
            Transition.fadeIn(imageView);

            closeButton.setVisible(true);
            openButton.setVisible(false);
            textToEncode.setDisable(false);
        }
    }

    @FXML
    private void handleSaveFile(ActionEvent event) {

    }

    @FXML
    private void handleEncode(ActionEvent event) {
        controller.injectUI(imageView, FinalImageView, textToEncode, resultText);
        controller.onEncode();
    }

    @FXML
    private void handleDecode(ActionEvent event) {
        controller.onDecode();
    }

    private Model makeModel() {
        return new Model(new TextEncoder(), new TextDecoder());
    }

}
