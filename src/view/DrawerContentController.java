package view;

import com.jfoenix.controls.JFXRippler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

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
}
