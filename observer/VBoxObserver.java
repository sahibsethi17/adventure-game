package observer;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * This class is used to update the background of a VBox depending on the contrast setting
 */
public class VBoxObserver implements NodeObserver{
    public VBox node;


    /**
     * Constructor for the VBoxObserver
     * @param vbox the vbox that is observing.
     * @param style the style of the vbox
     */
    public VBoxObserver(VBox vbox, String style){

        this.node = vbox;
        node.setStyle(style);
    }

    @Override
    /**
     * Update the background of the vbox
     */
    public void update(String value) {
        node.setStyle("-fx-background-color: " + value + ";");

    }
}