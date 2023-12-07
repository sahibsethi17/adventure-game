package observer;

import javafx.scene.control.*;

/**
 * This class is used to update a Button depending on the given colour setting

 */
public class BackButtonObserver implements NodeObserver{
    public Button node;

    /**
     * Constructor for the ButtonObserver
     * @param button the button that is observing.
     * @param style the style of the button
     */
    public BackButtonObserver(Button button, String style){

        button.setStyle(style);
        this.node = button;
    }

    @Override
    /**
     * Update the text colour of the button
     */
    public void update(String value) {
        node.setStyle(value);

    }
}