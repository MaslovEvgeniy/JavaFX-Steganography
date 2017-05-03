package view;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import utils.Transition;

public class ImageToImageController {
    @FXML
    private GridPane gridPane;

    @FXML
    private ImageView FinalImageView;

    @FXML
    private JFXButton openButton1;

    @FXML
    private FontAwesomeIconView but_open1;

    @FXML
    private StackPane stackPane1;

    @FXML
    private Rectangle rect1;

    @FXML
    private ImageView imageViewDrag;

    @FXML
    private ImageView imageView;

    @FXML
    private JFXButton closeButton;

    @FXML
    private FontAwesomeIconView but_close;

    @FXML
    private JFXButton openButton;

    @FXML
    private FontAwesomeIconView openInitBtn;

    @FXML
    private StackPane stackPaneHid;

    @FXML
    private Rectangle rect11;

    @FXML
    private ImageView imageViewDrag1;

    @FXML
    private ImageView imageView1;

    @FXML
    private JFXButton closeHidButton;

    @FXML
    private FontAwesomeIconView but_close1;

    @FXML
    private JFXButton openHidButton;

    @FXML
    private FontAwesomeIconView openHidBtn;

    @FXML
    private JFXButton encodeButton;

    @FXML
    private JFXTextField inputPath;

    @FXML
    private JFXTextField inputPath1;

    @FXML
    private JFXSnackbar snackBar;

    @FXML
    void handleDragOver(DragEvent event) {

    }

    @FXML
    void handleDrop(DragEvent event) {

    }

    @FXML
    void handleEntered(DragEvent event) {

    }

    @FXML
    void handleExited(DragEvent event) {

    }

    @FXML
    void handleOpenInitImage(ActionEvent event) throws IOException {
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

            Transition.fadeOut(imageViewDrag);
            Transition.fadeIn(imageView);

            stackPaneHid.setOpacity(1);
            stackPaneHid.setDisable(false);
            openHidButton.setDisable(false);
            openButton.setVisible(false);
            closeButton.setVisible(true);
            openHidButton.setVisible(true);
            encodeButton.setDisable(false);

            showSnackBar("Изображение добавлено");

        }
    }

    @FXML
    void handleCloseInitImage(ActionEvent event) throws  IOException {
        if (imageView.getImage() != null) {
            Transition.fadeOut(imageView);
            imageView.setImage(null);
            inputPath.setText("PATH");

            Transition.fadeIn(imageViewDrag);

            stackPaneHid.setOpacity(0.25);
            stackPaneHid.setDisable(true);
            openHidButton.setDisable(true);
            closeButton.setVisible(false);
            openButton.setVisible(true);
            encodeButton.setDisable(true);
        }
    }

    @FXML
    void handleOpenHidImage(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        fileChooser.getExtensionFilters().
                addAll(new FileChooser.ExtensionFilter("Image Files (*.bmp, *.png, *.jpg, *.jpeg)",
                        "*.bmp", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(imageView1.getScene().getWindow());
        if (file != null) {
            String imageFile = file.toURI().toURL().toString();

            Image image = new Image(imageFile);
            imageView1.setImage(image);

            String path = file.getPath();
            inputPath1.setText(path);

            Transition.fadeOut(imageViewDrag1);
            Transition.fadeIn(imageView1);

            stackPaneHid.setOpacity(1);
            stackPaneHid.setDisable(false);
            openHidButton.setDisable(true);
            closeHidButton.setVisible(true);
            encodeButton.setDisable(false);

            showSnackBar("Изображение добавлено");

        }
    }

    @FXML
    void handleCloseHidImage(ActionEvent event) throws  IOException {
        if (imageView1.getImage() != null) {
            Transition.fadeOut(imageView1);
            imageView1.setImage(null);
            inputPath1.setText("PATH");

            Transition.fadeIn(imageViewDrag1);

            openHidButton.setDisable(true);
            closeHidButton.setVisible(false);
            encodeButton.setDisable(true);
        }
    }

    private void showSnackBar(String message) {
        snackBar = new JFXSnackbar(gridPane);
        snackBar.show(message, 2000);
    }


}
