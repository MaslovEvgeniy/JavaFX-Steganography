package view;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import com.sun.istack.internal.logging.Logger;
import javafx.animation.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import sun.applet.Main;
import utils.*;
import utils.Transition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class MainWindowController implements Initializable {

    @FXML
    private JFXTextArea textToEncode;

    @FXML
    private JFXButton encodeButton;

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
    private StackPane stackPane1;

    @FXML
    private Rectangle rect1;


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
            imageView.setImage(img);
            closeButton.setVisible(true);
            openButton.setVisible(false);
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

            Transition.fadeIn(imageViewDrag);

            closeButton.setVisible(false);
            openButton.setVisible(true);
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

            Transition.fadeOut(imageViewDrag);
            Transition.fadeIn(imageView);

            closeButton.setVisible(true);
            openButton.setVisible(false);
        }

    }

}
