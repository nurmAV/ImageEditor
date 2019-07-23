package sample;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ControlPane extends Pane {

    private SharpnessTools sharpnessTools = new SharpnessTools();
    private ColorComponentTools colorComponentTools = new ColorComponentTools();

    public ControlPane() {
        this.getChildren().add( new Text("No file selected"));

    }
}
