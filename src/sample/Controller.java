package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public VBox layout;
    @FXML
    public ImageView workingImage;
    @FXML
    public MenuItem openMenuButton;
    @FXML
    public HBox middleRow;
    @FXML
    public Text filename;
    @FXML
    public Button blur;

    private BufferedImage workingBufferedImage = null;
    @Override
    public void initialize(URL location, ResourceBundle resources) {



    }

    public void open() {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(layout.getScene().getWindow());
        ((Stage) layout.getScene().getWindow()).setTitle(file.getName() + " - Image Editor");
        System.out.println(middleRow.heightProperty());
        workingImage.fitHeightProperty().bind(middleRow.heightProperty());
        //original.fitWidthProperty().bind(middleRow.widthProperty());
        try {
            BufferedImage bi = ImageIO.read(file);
            workingBufferedImage = bi;
            workingImage.setImage(SwingFXUtils.toFXImage(bi, null));
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void close() {
        Stage s = (Stage) layout.getScene().getWindow();
        s.close();
    }


    public BufferedImage blur(int size) {
        BufferedImage res = new BufferedImage(workingBufferedImage.getWidth(), workingBufferedImage.getHeight(), workingBufferedImage.getType());
        float[] data = new float[size * size];
        for(int i = 0; i < size * size; i++) {
            data[i] = 1f/(size*size);
        }
        Kernel kernel = new Kernel(size, size, data);
        ConvolveOp op = new ConvolveOp(kernel);
        op.filter(workingBufferedImage, res);
        return res;
    }
    public BufferedImage sobelY() {
        System.out.println("Sobel Y");
        BufferedImage dest = new BufferedImage(workingBufferedImage.getWidth(), workingBufferedImage.getHeight(), workingBufferedImage.getType());
        //workingImage.setImage(SwingFXUtils.toFXImage(bi, null));
        float[] data = {-1, 0, 1,
                        -2, 0, 2,
                        -1, 0, 1};
        ConvolveOp op = new ConvolveOp(new Kernel(3, 3, data));
        op.filter(workingBufferedImage, dest);
        workingBufferedImage = dest;
        workingImage.setImage(SwingFXUtils.toFXImage(workingBufferedImage, null));
        return dest;
    }

    public BufferedImage sobelX() {
        System.out.println("Sobel X");
        BufferedImage dest = new BufferedImage(workingBufferedImage.getWidth(), workingBufferedImage.getHeight(), workingBufferedImage.getType());
        //workingImage.setImage(SwingFXUtils.toFXImage(bi, null));
        float[] data = {-1, -2, -1,
                         0,  0,  0,
                         1,  2,  1};
        ConvolveOp op = new ConvolveOp(new Kernel(3, 3, data));
        op.filter(workingBufferedImage, dest);
        workingBufferedImage = dest;
        workingImage.setImage(SwingFXUtils.toFXImage(workingBufferedImage, null));
        return dest;
    }



}