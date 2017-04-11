package view;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import com.sun.istack.internal.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.SequentialTransitionBuilder;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
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
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED,(e)->{
            close.setRate(close.getRate()*-1);
            close.play();
            if(drawer.isShown()) {
                drawer.close();
            } else {
                drawer.open();
            }
        });
    }



    @FXML
    private void handleDragOver(DragEvent event){
        if(event.getDragboard().hasFiles()) {
            List<File> phil = event.getDragboard().getFiles();
            String path = phil.get(0).toPath().toString();

            if (path.endsWith(".jpeg") || path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".bmp")) {
                event.acceptTransferModes(TransferMode.ANY);
            }
        }
    }

    @FXML
    private void handleDrop(DragEvent event){
        try {
            List<File> files = event.getDragboard().getFiles();
            Image img = new Image(new FileInputStream(files.get(0)));
            imageView.setImage(img);
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEntered(DragEvent event){
        FillTransition ft = new FillTransition(Duration.millis(500), rect1, Color.valueOf("#E0E0E0"), Color.WHITE);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
        FadeTransition ftr = new FadeTransition(Duration.millis(500), imageView);
        ftr.setFromValue(1);
        ftr.setToValue(0);
        ftr.play();
        FadeTransition ftrd = new FadeTransition(Duration.millis(500), imageViewDrag);
        ftrd.setFromValue(0);
        ftrd.setToValue(1);
        ftrd.play();

    }

    @FXML
    private void handleExited(DragEvent event){
        FillTransition ft = new FillTransition(Duration.millis(500), rect1, Color.WHITE, Color.valueOf("#E0E0E0"));
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
        FadeTransition ftr = new FadeTransition(Duration.millis(500), imageView);
        ftr.setFromValue(0);
        ftr.setToValue(1);
        ftr.play();
        FadeTransition ftrd = new FadeTransition(Duration.millis(500), imageViewDrag);
        ftrd.setFromValue(1);
        ftrd.setToValue(0);
        ftrd.play();

    }


    @FXML
    void handleClose(ActionEvent event) {

    }


    @FXML
    private void handleOpenFile(ActionEvent event) throws IOException {

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG", "*.JPEG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        FileChooser.ExtensionFilter extFilterBMP = new FileChooser.ExtensionFilter("BMP files (*.bmp)", "*.BMP");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG, extFilterBMP);

        File file = fileChooser.showOpenDialog(imageView.getScene().getWindow());

        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(image);
            FadeTransition ftr = new FadeTransition(Duration.millis(1), imageViewDrag);
            ftr.setFromValue(0.5);
            ftr.setToValue(0);
            ftr.play();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
