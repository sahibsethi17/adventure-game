package observer;

import javafx.geometry.Insets;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * This class is used to update a GridPane that is being observed
 */
public class GridPaneObserver implements NodeObserver{
    public GridPane node;

    /**
     * Constructor for the GridPaneObserver
     * @param gridpane the gridpane that is observing.
     * @param style the colour setting of the gridpane
     */
    public GridPaneObserver(GridPane gridpane, String style){
        gridpane.setStyle(style);
        this.node = gridpane;

    }

    @Override

    /**
     * Update the background of the gridpane
     */
    public void update(String value) {
        node.setStyle("-fx-background-color: " + value + ";");
    }
}

