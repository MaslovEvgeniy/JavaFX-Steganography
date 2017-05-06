package view;


import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import sun.plugin.javascript.navig.Anchor;
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
    private AnchorPane rootPane;

    @FXML
    private VBox menu;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private JFXSnackbar snackBar;

    @FXML
    private AnchorPane menuTextToImage;

    @FXML
    private AnchorPane menuImageToImage;

    @FXML
    private AnchorPane menuHelp;

    @FXML
    private AnchorPane menuAbout;

    private static String FileExtention = null;

    private Controller controller = new Controller(makeModel());

    private Parent contentTextToImage = null;

    private Parent contentImageToImage = null;

    private Parent contentHelp = null;

    private Parent contentAbout = null;

    private HamburgerBasicCloseTransition close;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
           /*VBox box = FXMLLoader.load(getClass().getResource("DrawerContent.fxml"));
            drawer.setSidePane(box);*/

            contentTextToImage = FXMLLoader.load(getClass().getResource("TextToImage.fxml"));
            contentPane.getChildren().add(contentTextToImage);
            setAnchor(contentTextToImage);

            contentImageToImage = FXMLLoader.load(getClass().getResource("ImageToImage.fxml"));
            setAnchor(contentImageToImage);

            /*contentTextToImage = FXMLLoader.load(getClass().getResource("TextToImage.fxml")); //TODO ADD OTHER
            setAnchor(contentTextToImage);

            contentTextToImage = FXMLLoader.load(getClass().getResource("TextToImage.fxml"));
            setAnchor(contentTextToImage);*/

            menu.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> {
                showMenu();
            });

            menu.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> {
                hideMenu();
            });

            handleTextToImage(null);

        } catch (IOException e) {
            e.printStackTrace();
        }

        close = new HamburgerBasicCloseTransition(hamburger);
        close.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            if (menu.getWidth()== 270) {
                hideMenu();
            } else {
                showMenu();
            }
        });
    }

    private void hideMenu(){
        menu.setMinWidth(63);
        close.setRate(-1);
        close.play();
    }

    private void showMenu(){
        menu.setMinWidth(270);
        close.setRate(1);
        close.play();
    }

    private void setAnchor(Parent p){
        AnchorPane.setBottomAnchor(p, 0.0);
        AnchorPane.setTopAnchor(p, 0.0);
        AnchorPane.setLeftAnchor(p, 0.0);
        AnchorPane.setRightAnchor(p, 0.0);
    }

    private void selectMenuItem(Pane pane){
        for (Node n:
             menu.getChildren()) {
            Pane p = (Pane) n;
            for (Node img: p.getChildren())
                if (img instanceof FontAwesomeIconView)
                    ((FontAwesomeIconView)img).setStyle("-fx-fill: #747474");

            if (p == pane) {
                p.setStyle("-fx-background-color: #EEEEEE");
                for (Node img: p.getChildren())
                    if (img instanceof FontAwesomeIconView)
                        ((FontAwesomeIconView)img).setStyle("-fx-fill: #325bf5");
            }
            else
                p.setStyle("");
        }
    }

    @FXML
    void handleImageToImage(MouseEvent event) {
        selectMenuItem(menuImageToImage);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(contentImageToImage);
    }

    @FXML
    void handleTextToImage(MouseEvent event) {
        selectMenuItem(menuTextToImage);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(contentTextToImage);
    }

    private Model makeModel() {
        return new Model(new TextEncoder(), new TextDecoder());
    }


}
