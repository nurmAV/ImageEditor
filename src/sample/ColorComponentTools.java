package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class ColorComponentTools extends VBox {

    @FXML public Slider redSlider, greenSlider, blueSlider, alphaSlider;

    public ColorComponentEffect redness   = new ColorComponentEffect(Color.RED);
    public ColorComponentEffect greenness = new ColorComponentEffect(Color.GREEN);
    public ColorComponentEffect blueness  = new ColorComponentEffect(Color.BLUE);
    public ColorComponentEffect alpha     = new ColorComponentEffect(Color.ALPHA);


    public Slider getRedSlider() {
        return redSlider;
    }

    public Slider getBlueSlider() {
        return blueSlider;
    }

    public Slider getGreenSlider() {
        return greenSlider;
    }

    public Slider getAlphaSlider() {
        return alphaSlider;
    }

    public ColorComponentTools() {
        FXMLLoader loader  = new FXMLLoader(getClass().getResource("../view/ColorComponentTools.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
