package view;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

public class HelpController {

    @FXML
    private Line line1;

    @FXML
    private Line line2;

    @FXML
    private AnchorPane firstStepPane;

    @FXML
    private TextArea firstStepText;

    @FXML
    private JFXButton firstStepButton;

    @FXML
    private AnchorPane secondStepPane;

    @FXML
    private TextArea secondStepText;

    @FXML
    private JFXButton secondStepButton;

    @FXML
    private AnchorPane thirdStepPane;

    @FXML
    private TextArea thirdStepText;

    @FXML
    private JFXButton thirdStepButton;

    @FXML
    void handleFirstStep(ActionEvent event) {
       handleStepButton(firstStepText, firstStepPane);
    }

    @FXML
    void handleSecondStep(ActionEvent event) {
        handleStepButton(secondStepText, secondStepPane);
    }

    @FXML
    void handleThirdStep(ActionEvent event) {
        handleStepButton(thirdStepText, thirdStepPane);
    }

    private void handleStepButton(TextArea text, AnchorPane pane ){
        if(text.getOpacity()!=0) {
            text.setOpacity(0);
            pane.setPrefHeight(50);
        }
        else{
            text.setOpacity(1);
            pane.setPrefHeight(text.getPrefHeight()+100);
        }

    }

}
