package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML public VBox layout;
    @FXML public ImageView original;
    @FXML public ImageView changed;
    @FXML public MenuItem openMenuButton;
    @FXML public HBox middleRow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    public void open() {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(layout.getScene().getWindow());
        ((Stage)layout.getScene().getWindow()).setTitle(file.getName() + " - Image Editor");
        System.out.println(middleRow.heightProperty());
        original.fitHeightProperty().bind(middleRow.heightProperty());
        //original.fitWidthProperty().bind(middleRow.widthProperty());
        try {
            original.setImage(SwingFXUtils.toFXImage(ImageIO.read(file), null));
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void close() {
        Stage s = (Stage) layout.getScene().getWindow();
        s.close();
    }
}
