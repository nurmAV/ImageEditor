package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javafx.scene.input.KeyEvent;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.util.Stack;

public class Controller implements Initializable {

    @FXML
    public VBox layout, controlVBox, fileInfo;
    @FXML
    public ImageView workingImage;
    @FXML
    public MenuItem openMenuButton, undoItem;
    @FXML
    public HBox middleRow, toolRibbon;
    @FXML
    public Text filename;
    @FXML public Label status;
    @FXML
    public Button blur, sobelX, sobelY, edge, grayscale, redness, greenness, blueness, alpha, text;
    @FXML public AnchorPane imageBG;

    public ControlPane controlPane = new ControlPane();
    public SharpnessTools sharpnessTools = controlPane.getSharpnessTools();
    public ColorComponentTools colorComponentTools = controlPane.getColorComponentTools();

    public Slider redSlider = colorComponentTools.getRedSlider();
    public Slider greenSlider = colorComponentTools.getGreenSlider();
    public Slider blueSlider = colorComponentTools.getBlueSlider();
    public Slider alphaSlider = colorComponentTools.getAlphaSlider();
    private BufferedImage workingBufferedImage   = null;

    private ColorComponentEffect alphaEffect     = new ColorComponentEffect(Color.ALPHA);
    private ColorComponentEffect rednessEffect   = new ColorComponentEffect(Color.RED);
    private ColorComponentEffect greennessEffect = new ColorComponentEffect(Color.GREEN);
    private ColorComponentEffect bluenessEffect  = new ColorComponentEffect(Color.BLUE);
    private TextEffect textEffect = new TextEffect();

    private Stack<BufferedImage> imageStack = new Stack<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        blur     .setOnAction(   e -> changeImage(blur(20)));
        sobelX   .setOnAction(   e -> changeImage(sobelX()));
        sobelY   .setOnAction(   e -> changeImage(sobelY()));
        edge     .setOnAction(   e -> changeImage(completeSobel()));
        grayscale.setOnAction(e -> changeImage(grayscale()));

        redness  .setOnMouseClicked(e -> changeImage(rednessEffect.filter(workingBufferedImage, 1.5)));
        greenness.setOnMouseClicked(e -> changeImage(greennessEffect.filter(workingBufferedImage, 1.5)));
        blueness .setOnMouseClicked(e -> changeImage(bluenessEffect.filter(workingBufferedImage, 1.5)));
        alpha    .setOnMouseClicked(e -> changeImage(alphaEffect.filter(workingBufferedImage, 1.5)));
        //controlVBox.getChildren().add(new SharpnessTools());

        //text.setOnMouseClicked(e ->changeImage(textEffect.apply(workingBufferedImage, "Text",  50, 50, new Color())));

        fileInfo.getChildren().add(controlPane);
        undoItem.setOnAction( e -> undo());
        undoItem.setDisable(true);

        redSlider.valueProperty().addListener(
                e -> changeImage(rednessEffect.filterThreaded(workingBufferedImage,  redSlider.getValue())));

        greenSlider.valueProperty().addListener(
                e -> changeImage(greennessEffect.filterThreaded(workingBufferedImage, greenSlider.getValue())));

        blueSlider.valueProperty().addListener(
                e -> changeImage(bluenessEffect.filterThreaded(workingBufferedImage, blueSlider.getValue())));

        alphaSlider.valueProperty().addListener(
                e -> changeImage(alphaEffect.filterThreaded(workingBufferedImage, alphaSlider.getValue())));
    }


    public void open() {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(layout.getScene().getWindow());
        ((Stage) layout.getScene().getWindow()).setTitle(file.getName() + " - Image Editor");
        System.out.println(middleRow.heightProperty());
        workingImage.fitWidthProperty().bind(imageBG.widthProperty());
        try {
            BufferedImage bi = ImageIO.read(file);
            workingBufferedImage = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);

            workingBufferedImage.getGraphics().drawImage(bi, 0, 0, null);
            workingImage.setImage(SwingFXUtils.toFXImage(bi, null));
            //workingImage.fitHeightProperty().bind(imageBG.heightProperty());
            imageStack.push(workingBufferedImage);
            System.out.println("Image Stack Size: " + imageStack.size());
            layout.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(event.isControlDown() && event.getCode() == KeyCode.Z) undo();

                }
            });
        } catch (IOException e) {

            e.printStackTrace();
        }
        catch(NullPointerException e){

        }
    }

    /** Closes the main window */
    public void close() {
        Stage s = (Stage) layout.getScene().getWindow();
        s.close();
    }

    /**
     * Performs a standard mean blur
     * @param size The width of the convolution kernel, i.e. how much blur asked for
     * @return Blurred image as BufferedImage
     */
    public BufferedImage blur(int size) {
        return blur(workingBufferedImage, size);
    }


    public BufferedImage blur(BufferedImage img, int size) {
        changeStatus("Blurring...");
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        float[] data = new float[size * size];
        for(int i = 0; i < size * size; i++) {
            data[i] = 1f/(size*size);
        }
        Kernel kernel = new Kernel(size, size, data);
        ConvolveOp op = new ConvolveOp(kernel);
        op.filter(img, res);

        return res;
    }

    /**
     * Performs a vertical (Y-direction) Sobel filtration on given image
     * @param img the image to be filtered
     * @return the filtered image as BufferedImage
     */
    public BufferedImage sobelY(BufferedImage img) {
        changeStatus("Sobel Y ...");
        BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        float[] data = {-1, 0, 1,
                        -2, 0, 2,
                        -1, 0, 1};
        ConvolveOp op = new ConvolveOp(new Kernel(3, 3, data));
        op.filter(img, dest);

        return dest;
    }
    /**
     * Performs a horizontal(X-direction) Sobel filtration on given image
     * @param img the image to be filtered
     * @return the filtered image as BufferedImage
     */
    public BufferedImage sobelX(BufferedImage img) {
        changeStatus("Sobel X ...");
        BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        float[] data = {-1, -2, -1,
                         0,  0,  0,
                         1,  2,  1};
        ConvolveOp op = new ConvolveOp(new Kernel(3, 3, data));
        op.filter(img, dest);

        return dest;
    }

    public BufferedImage sobelX() {
        return sobelX(workingBufferedImage);
    }

    public BufferedImage sobelY() {
        return sobelY(workingBufferedImage);
    }


    public BufferedImage completeSobel(BufferedImage img) {

        BufferedImage res = blur(grayscale(),5);

        BufferedImage x = sobelX(res);
        BufferedImage y = sobelY(res);

        changeStatus("Combinging...");
        for(int i = 0; i < x.getWidth(); i++) {
            for(int j = 0; j < x.getHeight(); j++) {
                int c1 = x.getRGB(i, j);
                int c2 = y.getRGB(i,j);
                res.setRGB(i,j, pixelOp(c1, c2));
            }
        }

        return res;
    }

    private int pixelOp(int c1, int c2) {
        int r1 = (c1 & 0x00ff0000) >> 16;
        int g1 = (c1 & 0x0000ff00) >> 8;
        int b1 =  c1 & 0x000000ff;

        int r2 = (c2 & 0x00ff0000) >> 16;
        int g2 = (c2 & 0x0000ff00) >> 8;
        int b2 =  c2 & 0x000000ff;

        int r = (int) Math.sqrt(r1 * r1 + r2 * r2);
        int g = (int) Math.sqrt(g1 * g1 + g2 * g2);
        int b = (int) Math.sqrt(b1 * b1 + b2 * b2);

        int res = (r << 16) + (g << 8) + b;
        return res;


    }

    public BufferedImage completeSobel() {
        return completeSobel(workingBufferedImage);
    }

    public BufferedImage grayscale(BufferedImage img) {
        BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for(int x = 0; x < img.getWidth(); x++) {
            for(int y = 0; y < img.getHeight(); y++) {
                int c = img.getRGB(x,y);
                int r = (c & 0x00ff0000) >> 16;
                int g = (c & 0x0000ff00) >> 8;
                int b =  c & 0x000000ff;

                int avg = (int)((r + g + b) /3f);
                int newColor = (avg << 16) + (avg << 8) + avg;
                res.setRGB(x,y, newColor);
            }
        }
        return res;
    }

    public BufferedImage grayscale() {
        return grayscale(workingBufferedImage);
    }

    private void changeImage(BufferedImage img) {
        if(undoItem.isDisable()) undoItem.setDisable(false);
        workingBufferedImage = img;
        imageStack.push(workingBufferedImage);
        workingImage.setImage(SwingFXUtils.toFXImage(img, null));
        System.out.println("Image Stack Size: " +imageStack.size());
        changeStatus("Ready.");
    }

    public void undo() {
        if ((imageStack.size() > 1)) {

            imageStack.pop(); // Remove the current image from the stack
            workingImage.setImage(SwingFXUtils.toFXImage(imageStack.peek(), null));
            System.out.println("Image Stack Size: " +imageStack.size());
        }else undoItem.setDisable(true);
    }

    public void changeStatus(String statusText) {
        status.setText(statusText);
    }


}
