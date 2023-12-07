package observer;

import javafx.scene.control.*;

/**
 * This class is used to update a Button depending on the given colour setting

 */
public class TextButtonObserver implements NodeObserver{
    public Button node;
    /**
     * Constructor for the ButtonObserver
     * @param button the button that is observing.
     * @param style the background and text colour of the button
     */
    public TextButtonObserver(Button button, String style){


        this.node = button;
        node.setStyle(style);

    }
    /**
     * Update the text colour of the button
     */
    @Override

    public void update(String value) {
        node.setStyle(value);
    }
}