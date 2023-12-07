package observer;

import javafx.scene.paint.Color;

import javafx.scene.text.Text;

/**
 * This class is used to update a Label f
 */
public class TextObserver implements NodeObserver{
    public Text node;
    /**
     * Constructor for TextObserver
     * @param text the text to be updated
     * @param style the colour of the text
     */
    public TextObserver(Text text, String style){
        text.setStyle(style);
        this.node = text;
    }
    /**
     * Update the text colour of the text
     */
    @Override
    public void update(String value) {
        node.setStyle("-fx-text-fill: " + value + ";");
    }
}

