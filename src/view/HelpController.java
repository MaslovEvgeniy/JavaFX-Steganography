package view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

public class HelpController {

    @FXML
    private AnchorPane firstStepPane;

    @FXML
    private JFXButton firstStepButton;

    @FXML
    private JFXTextArea firstStepText;

    @FXML
    private AnchorPane line1;

    @FXML
    private AnchorPane secondStepPane;

    @FXML
    private JFXTextArea secondStepText;

    @FXML
    private JFXButton secondStepButton;

    @FXML
    private AnchorPane line2;

    @FXML
    private AnchorPane thirdStepPane;

    @FXML
    private JFXButton thirdStepButton;

    @FXML
    private JFXTextArea thirdStepText;

    @FXML
    void handleFirstStep(ActionEvent event) {
        if(firstStepText.getOpacity()!=0) {
            line1.setPrefHeight(15);
        }
        else{
            line1.setPrefHeight(140);
        }
       handleStepButton(firstStepText, firstStepPane);
    }

    @FXML
    void handleSecondStep(ActionEvent event) {
        if(secondStepText.getOpacity()!=0) {
            line2.setPrefHeight(15);
        }
        else{
            line2.setPrefHeight(140);
        }
        handleStepButton(secondStepText, secondStepPane);
    }

    @FXML
    void handleThirdStep(ActionEvent event) {
        handleStepButton(thirdStepText, thirdStepPane);
    }

    private void handleStepButton(TextArea text, AnchorPane pane ){
        if(text.getOpacity()!=0) {
            text.setOpacity(0);
            pane.setPrefHeight(70);
        }
        else{
            text.setOpacity(1);
            pane.setPrefHeight(text.getPrefHeight()+60);
        }
    }
}
