package model;

import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Немченко Анна on 24.04.2017.
 */
public class Controller {

    private Model model;

    public Controller(Model model){
        this.model=model;
    }

    private ImageView originalView, modifiedView;
    private TextArea text, resultText;

    public void injectUI(ImageView original, ImageView modified, TextArea text, TextArea resultText) {
        this.originalView = original;
        this.modifiedView = modified;
        this.text = text;
        this.resultText=resultText;
    }

    public void onEncode() {
        Image modified = model.encode(originalView.getImage(), text.getText());
        modifiedView.setImage(modified);
    }

    public void onDecode() {
        String message = model.decode(modifiedView.getImage());
        resultText.setText(message);
     }
}
