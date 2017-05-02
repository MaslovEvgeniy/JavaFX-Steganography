package view;

import com.jfoenix.controls.JFXRippler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Maslov on 30.03.2017.
 */
public class DrawerContentController implements Initializable {

    @FXML
    private VBox vbox;

    @FXML
    private HBox textToImage;

    @FXML
    private HBox imageToImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    void openAbout(MouseEvent event) {

    }

    @FXML
    void openImgToImg(MouseEvent event) throws IOException {
        Stage stage;
        Parent root;

        stage = (Stage) vbox.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("ImageToImage.fxml"));

        Pane pane = (Pane) vbox.getScene().lookup("#contentPane");
        pane.getChildren().clear();
        pane.getChildren().add(root);
    }

    @FXML
    void openTextToImage(MouseEvent event) throws IOException {
        Stage stage;
        Parent root;

        stage = (Stage) vbox.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("TextToImage.fxml"));

        Pane pane = (Pane) vbox.getScene().lookup("#contentPane");
        pane.getChildren().clear();
        pane.getChildren().add(root);
    }
}
