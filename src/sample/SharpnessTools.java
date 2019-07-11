package sample;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class SharpnessTools extends HBox {

    @FXML public Slider blurSlider, sharpenSlider;

    public SharpnessTools() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SharpnessTools.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
