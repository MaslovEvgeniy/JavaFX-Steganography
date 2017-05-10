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
            showStep(secondStepText, secondStepPane, 15, 140);
            hideStep(thirdStepText, thirdStepPane);
            hideStep(firstStepText, firstStepPane);

        }
        else{
            showStep(firstStepText, firstStepPane, 140, 15);
            hideStep(secondStepText, secondStepPane);
            hideStep(thirdStepText, thirdStepPane);
        }
    }

    @FXML
    void handleSecondStep(ActionEvent event) {
        if(secondStepText.getOpacity()!=0) {
            showStep(thirdStepText, thirdStepPane, 15, 15);
            hideStep(secondStepText, secondStepPane);
            hideStep(firstStepText, firstStepPane);

        }
        else{
            showStep(secondStepText, secondStepPane, 15, 140);
            hideStep(firstStepText, firstStepPane);
            hideStep(thirdStepText, thirdStepPane);
        }
    }

    @FXML
    void handleThirdStep(ActionEvent event) {
        if(thirdStepText.getOpacity()!=0) {
            showStep(firstStepText, firstStepPane, 140, 15);
            hideStep(thirdStepText, thirdStepPane);
            hideStep(secondStepText, secondStepPane);
        }
        else{
            showStep(thirdStepText, thirdStepPane, 15, 15);
            hideStep(secondStepText, secondStepPane);
            hideStep(firstStepText, firstStepPane);
        }
    }

    private void hideStep(TextArea text, AnchorPane pane){
        text.setOpacity(0);
        pane.setPrefHeight(70);
    }

    private void showStep(TextArea text, AnchorPane pane, double a, double b){
        line1.setPrefHeight(a);
        line2.setPrefHeight(b);
        text.setOpacity(1);
        pane.setPrefHeight(text.getPrefHeight()+60);
    }
}
