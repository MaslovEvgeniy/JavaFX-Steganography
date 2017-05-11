package view;


import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
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

    @FXML
    private AnchorPane menuExit;

    private Parent contentTextToImage = null;

    private Parent contentImageToImage = null;

    private Parent contentHelp = null;

    private Parent contentAbout = null;

    private HamburgerBasicCloseTransition close;

    /**
     * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            contentTextToImage = FXMLLoader.load(getClass().getResource("TextToImage.fxml"));
            contentPane.getChildren().add(contentTextToImage);
            setAnchor(contentTextToImage);

            contentImageToImage = FXMLLoader.load(getClass().getResource("ImageToImage.fxml"));
            setAnchor(contentImageToImage);

            contentHelp = FXMLLoader.load(getClass().getResource("Help.fxml"));
            setAnchor(contentHelp);

            contentAbout = FXMLLoader.load(getClass().getResource("About.fxml"));
            setAnchor(contentAbout);

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

    /**
     * Hide vertical menu
     */
    private void hideMenu(){
        menu.setMinWidth(63);
        close.setRate(-1);
        close.play();
    }

    /**
     * Show vertical menu
     */
    private void showMenu(){
        menu.setMinWidth(270);
        close.setRate(1);
        close.play();
    }

    /**
     * Set specific pane ?????????? //TODO
     */
    private void setAnchor(Parent p){
        AnchorPane.setBottomAnchor(p, 0.0);
        AnchorPane.setTopAnchor(p, 0.0);
        AnchorPane.setLeftAnchor(p, 0.0);
        AnchorPane.setRightAnchor(p, 0.0);
    }

    /**
     * Customize menu item if it is selected
     * @param pane
     */
    private void selectMenuItem(Pane pane){
        for (Node n:
             menu.getChildren()) {
            Pane p = (Pane) n;
            for (Node img: p.getChildren())
                if (img instanceof FontAwesomeIconView)
                    (img).setStyle("-fx-fill: #747474");

            if (p == pane) {
                p.setStyle("-fx-background-color: #EEEEEE");
                for (Node img: p.getChildren())
                    if (img instanceof FontAwesomeIconView)
                        (img).setStyle("-fx-fill: #325bf5");
            }
            else
                p.setStyle("");
        }
    }

    /**
     * Select menu item for Image-to-Image encoding
     * @param event click
     */
    @FXML
    void handleImageToImage(MouseEvent event) {
        selectMenuItem(menuImageToImage);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(contentImageToImage);
    }

    /**
     * Select menu item for Text-to-Image encoding
     * @param event click
     */
    @FXML
    void handleTextToImage(MouseEvent event) {
        selectMenuItem(menuTextToImage);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(contentTextToImage);
    }

    /**
     * Select menu item for Help
     * @param event click
     */
    @FXML
    void handleHelp(MouseEvent event) {
        selectMenuItem(menuHelp);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(contentHelp);
    }

    /**
     * Select menu item for About
     * @param event click
     */
    @FXML
    void handleAbout(MouseEvent event) {
        selectMenuItem(menuAbout);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(contentAbout);
    }

    /**
     * Select menu item for Exit
     * @param event click
     */
    @FXML
    void handleExit(MouseEvent event) {
        Platform.exit();
    }

}
