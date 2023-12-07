package AdventureModel;

import java.io.Serializable; //you will need this to save the game!

/**
 * This class keeps track of the props or the objects in the game.
 * These objects have a name, description, and location in the game.
 * The player with the objects can pick or drop them as they like and
 * these objects can be used to pass certain passages in the game.
 */
public class AdventureObject implements Serializable {
    /**
     * The name of the object.
     */
    private String objectName;

    /**
     * The description of the object.
     */
    private String description;

    /**
     * The location of the object.
     */
    private Room location = null;

    /**
     * The amount of opinion points a character gains if gifted an object.
     */
    private int opinionReward;

    /**
     * Tracks whether objects are giftable to characters.
     */
    private boolean giftable = false;

    /**
     * Adventure Object Constructor
     * ___________________________
     * This constructor sets the name, description, and location of the object.
     *
     * @param name The name of the Object in the game.
     * @param description One line description of the Object.
     * @param location The location of the Object in the game.
     */
    public AdventureObject(String name, String description, Room location){
        this.objectName = name;
        this.description = description;
        this.location = location;
    }

    /**
     * Getter method for the name attribute.
     *
     * @return name of the object
     */
    public String getName(){
        return this.objectName;
    }

    /**
     * Getter method for the description attribute.
     *
     * @return description of the game
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * This method returns the location of the object if the object is still in
     * the room. If the object has been pickUp up by the player, it returns null.
     *
     * @return returns the location of the object if the objects is still in
     * the room otherwise, returns null.
     */
    public Room getLocation(){
        return this.location;
    }

    /**
     * This method checks whether objects are giftable.
     *
     * @return true if an object is giftable.
     */
    public boolean isGiftable() {
        return this.giftable;
    }

    /**
     * This method sets objects as giftable.
     */
    public void setGift() {
        this.giftable = true;
    }

    /**
     * This method gets the opinion points a character gains when gifted an object.
     *
     * @return the amount of opinion points.
     */
    public int getOpinionReward() {
        return this.opinionReward;
    }

    /**
     * This method sets the opinion points a character gains when gifted an object.
     *
     * @param opinion the amount of opinion points.
     */
    public void setOpinionReward(int opinion) {
        this.opinionReward = opinion;
    }

}
