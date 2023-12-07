package observer;

import javafx.scene.control.*;

import javafx.scene.paint.Color;

/**
 * This class is used to update a Label's text colour.
 */
public class LabelObserver implements NodeObserver{
    public Label label;

    /**
     * Constructor for the LabelObserver
     * @param label the label that is observing.
     * @param style the current label colour
     */
    public LabelObserver(Label label, String style){

        label.setStyle(style);
        this.label = label;



    }
    /**
     * Update the text colour of the label
     */
    @Override
    public void update(String value) {
        label.setStyle("-fx-text-fill: " + value + ";");
    }
}

