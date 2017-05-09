package view;

import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import utils.Transition;

public class AboutController {

    @FXML
    private AnchorPane placeForPhotoEugene;

    @FXML
    private AnchorPane placeForPhotoAnna;

    @FXML
    private ImageView photoEugene;

    @FXML
    private ImageView photoAnna;

    @FXML
    private void initialize() {
        alignment(photoEugene, placeForPhotoEugene);
        alignment(photoAnna, placeForPhotoAnna);
    }


    private void alignment(ImageView photo, AnchorPane placeForPhoto) {

        placeForPhoto.widthProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(photo);
        });

        placeForPhoto.heightProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(photo);
        });

        photo.imageProperty().addListener((obs, oldV, newV) -> {
            Transition.LayoutImage(photo);
        });

        Transition.LayoutImage(photo);

    }

}
