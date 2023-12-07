package AdventureModel;

import AdventureModel.Moods.Mood;

import java.util.ArrayList;

/**
 * The NPC interface. Used to create NPCs around the map of the Game.
 */
public interface NPC {

    /**
     * The name of the NPC.
     */
    String name = null;

    /**
     * Getter method for the name attribute.
     *
     * @return name of the NPC
     */
    String getName();

    /**
     * Getter method for the text attribute.
     *
     * @return dialogue of the NPC
     */
    String getText();

    /**
     * Getter method for the options attribute.
     *
     * @return array of interactions for the NPC
     */
    String[] getOptions();

    /**
     * Getter method for the currentRoom attribute.
     *
     * @return current room that the NPC is in
     */
    Room getCurrentRoom();

    /**
     * Getter method for the visited attribute.
     *
     * @return status of if the NPC has been visited
     */
    boolean isVisited();

    /**
     * Setter method for the visited attribute. Method updates when an NPC has been visited.
     */
    void setVisited();
}
