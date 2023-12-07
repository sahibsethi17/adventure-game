package AdventureModel;

import AdventureModel.Moods.Friendly;
import AdventureModel.Moods.Mood;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class implements the NPC Interface.
 * These objects have a name, a current room, a mood, and a list of optional hints, depending on their mood.
 * The player will get to interact with the Human by selecting an action from the Human's list of options.
 */
public class Human implements NPC, Serializable {

    @Serial
    private static final long serialVersionUID = -6731425071213512513L;

    /**
     * The name of the Human.
     */
    private String name;
    /**
     * The current room of the Human.
     */
    private Room currentRoom;
    /**
     * To check if this Human has already been visited.
     */
    private boolean visited;
    /**
     * The opening text from a Human.
     */
    private String text;
    /**
     * A list of options for the Player to interact with the Human NPC with.
     */
    private String[] options;
    /**
     * The mood of the Human NPC.
     */
    private Mood mood;
    /**
     * A fixed list of hints that will be added to the end of the Human NPC's opening dialogue, depending on their mood.
     */
    private ArrayList<String> hints = new ArrayList<>();

    /**
     * Human NPC Constructor
     * ___________________________
     * This constructor sets the name, current room, status, and hints of the Human NPC.
     *
     * @param n The name of the Human NPC in the game.
     * @param c Current room of the Human NPC.
     */
    public Human(String n, Room c) {
        this.name = n;
        this.currentRoom = c;
        this.visited = false;
        this.hints.add("Be careful going inside the building. There may be a troll there. A tip to defeat the troll, Neil Armstrong was the first person on the Moon, not in space, and Lance Armstrong isn't even a person.");
        this.hints.add("There's a troll by the valley, beside the stream. To defeat him, you must answer his riddle to pass. I've heard that Toronto and Edmonton are not capital cities of Canada. Take that advice as you wish.");
        this.hints.add("If you come along the rock with the slit in it, try going SOUTH/DOWN/WEST.");
        this.hints.add("When you're inside the building, take a good look around, you might find something useful going up the STAIRS or through the TRAPDOOR. I've said too much. Goodbye.");
        this.hints.add("If you see a water bird, PICK IT UP! It'll be useful.");
    }

    /**
     * setInteraction
     * ____________________________________________
     * This method sets the text & options of the Human NPC.
     *
     * @param text Human NPC Dialogue to be displayed.
     * @param options List of interaction for the Human NPC.
     */
    public void setInteraction(String text, String[] options) {
        this.text = text;
        this.options = options;
    }

    /**
     * Getter method for the name attribute.
     *
     * @return name of the Human NPC
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter method for the text attribute.
     *
     * @return dialogue of the Human NPC
     */
    public String getText() {
        return this.text;
    }

    /**
     * Getter method for the options attribute.
     *
     * @return array of interactions for the Human NPC
     */
    public String[] getOptions() {
        return this.options;
    }

    /**
     * Getter method for the visited attribute.
     *
     * @return status of if the Human NPC has been visited
     */
    public boolean isVisited() {
        return this.visited;
    }

    /**
     * Getter method for the currentRoom attribute.
     *
     * @return current room that the Human NPC is in
     */
    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    /**
     * Getter method for the mood attribute.
     *
     * @return mood of the Human NPC
     */
    public Mood getMood() {
        return this.mood;
    }

    /**
     * Setter method for the visited attribute. Method updates when Human NPC has been visited.
     */
    public void setVisited() {
        this.visited = true;
    }

    /**
     * Setter method for the mood attribute.
     *
     * @param m Mood of the Human NPC
     */
    public void setMood(Mood m) {
        this.mood = m;
    }

    /**
     * Method to add a hint at the end of the Human NPC dialogue. The dialogue will be updated only if this Human NPC is generous.
     */
    public void updateText() {
        if (this.mood.isGenerous()) {
            Collections.shuffle(this.hints);
            this.text = this.text + hints.get(0);
        }
    }

}
