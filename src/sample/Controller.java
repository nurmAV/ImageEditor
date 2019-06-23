package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML public BorderPane layout;
    @FXML public ImageView original;
    @FXML public ImageView changed;
    @FXML public MenuItem openMenuButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void open() {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(layout.getScene().getWindow());
        try {
            original.setImage(SwingFXUtils.toFXImage(ImageIO.read(file), null));
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
