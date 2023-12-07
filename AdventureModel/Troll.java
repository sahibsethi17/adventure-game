package AdventureModel;

import AdventureModel.Moods.Mood;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class implements the NPC Interface.
 * These objects have a name, a current room, a mood, and a status to see if they have been visited or not.
 * The player will get to interact with the Troll by selecting an option from the Troll's list of options.
 * If the player selects the correct option, they may move on, otherwise an item from their inventory is taken away
 */
public class Troll implements NPC, Serializable {

    @Serial
    private static final long serialVersionUID = -6731425071213512513L;

    /**
     * The name of the Troll.
     */
    private String name;
    /**
     * The name of the current room of the Troll.
     */
    private Room currentRoom;
    /**
     * To check if this Troll has already been visited.
     */
    private boolean visited;
    /**
     * The opening text from a Troll.
     */
    private String text;
    /**
     * A list of options for the Player to interact with the Troll NPC with.
     */
    private String[] options;
    /**
     * The correct option to select for the Troll NPC.
     */
    private String correctOption;
    /**
     * The text to display for the Troll NPC if the Player selects the correct option.
     */
    private String winningText;
    /**
     * The text to display for the Troll NPC if the Player selects the incorrect option.
     */
    private String losingText;
    /**
     * An optional object attribute that will be null, unless a Troll takes away something from a Player's inventory if an incorrect option was selected.
     */
    private AdventureObject object = null;
    /**
     * An alternate text for when the Player loses to the Troll NPC after the first time.
     */
    private String alternateLosingText = "YOU LOST!";

    /**
     * Troll NPC Constructor
     * ___________________________
     * This constructor sets the name, current room, and status of the Troll NPC.
     *
     * @param n The name of the Human NPC in the game.
     * @param c Current room of the Human NPC.
     */
    public Troll(String n, Room c) {
        this.name = n;
        this.currentRoom = c;
        this.visited = false;
    }

    /**
     * setInteraction
     * ___________________________
     * This method sets the text, options, correct option, winning text, and losing text of the Troll NPC.
     *
     * @param text The text of the Troll NPC in the game.
     * @param options Options for the Troll NPC.
     * @param correct Correct option of the Troll NPC
     * @param winningText Winning text of the Troll NPC
     * @param losingText Losing text of the Troll NPC
     */
    public void setInteraction(String text, String[] options, String correct, String winningText, String losingText) {
        this.text = text;
        this.options = options;
        this.correctOption = correct;
        this.winningText = winningText;
        this.losingText = losingText;
    }

    /**
     * Getter method for the name attribute.
     *
     * @return name of the Troll NPC
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter method for the currentRoom attribute.
     *
     * @return current room that the Troll NPC is in
     */
    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    /**
     * Getter method for the text attribute.
     *
     * @return dialogue of the Troll NPC
     */
    public String getText() {
        return this.text;
    }

    /**
     * Getter method for the options attribute.
     *
     * @return array of options for the Troll NPC
     */
    public String[] getOptions() {
        return this.options;
    }

    /**
     * Getter method for the visited attribute.
     *
     * @return status of if the Troll NPC has been visited
     */
    public boolean isVisited() {
        return this.visited;
    }

    /**
     * Getter method for the correctOption attribute.
     *
     * @return correct option from the list of options
     */
    public String getCorrectOption() {
        return this.correctOption;
    }

    /**
     * Getter method for the winningText attribute.
     *
     * @return text to display if Player selects correct option
     */
    public String getWinningText() {
        return this.winningText;
    }

    /**
     * Getter method for the losingText attribute.
     *
     * @return text to display if Player selects incorrect option
     */
    public String getLosingText() {
        return this.losingText;
    }

    /**
     * Setter method for the object attribute.
     *
     * @param a Adventure Object for Troll NPC to hold if Player selects incorrect option.
     */
    public void setObject(AdventureObject a) {
        this.object = a;
    }

    /**
     * Getter method for the object attribute.
     *
     * @return Adventure Object that Troll NPC is carrying.
     */
    public AdventureObject getObject() {
        return this.object;
    }

    /**
     * Getter method for the alternateLosingText attribute.
     *
     * @return text to display if Player selects incorrect option after the first time.
     */
    public String getAlternateLosingText() {
        return this.alternateLosingText;
    }

    /**
     * Setter method for the visited attribute. Called after the first interaction with the Troll NPC.
     */
    public void setVisited() {
        this.visited = true;
    }
}

