package sample;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ControlPane extends Pane {

    private SharpnessTools sharpnessTools = new SharpnessTools();
    private ColorComponentTools colorComponentTools = new ColorComponentTools();

    public SharpnessTools getSharpnessTools() {
        return sharpnessTools;
    }

    public ColorComponentTools getColorComponentTools() {
        return colorComponentTools;
    }

    public ControlPane() {
        this.getChildren().add( new Text("No file selected"));
        this.getChildren().add(colorComponentTools);

    }
}
