package observer;

import javafx.scene.control.*;

import javafx.scene.paint.Color;

/**
 * This class is used to update the background of a ScrollPane depending on the contrast setting
 */


public class ScrollPaneObserver implements NodeObserver{
    public ScrollPane node;


    /**
     * Constructor for the ScrollPaneObserver
     * @param scrollPane the scrollpane that is observing
     * @param style the background colour of the scrollpane
     */
    public ScrollPaneObserver(ScrollPane scrollPane, String style){
        this.node = scrollPane;
        node.setStyle(style);
    }

    @Override
    /**
     * Update the background of the scrollpane
     */
    public void update(String value) {

        node.setStyle("-fx-background: " + value + "; -fx-background-color:transparent;");

    }
}