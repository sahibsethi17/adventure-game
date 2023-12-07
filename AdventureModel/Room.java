package AdventureModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class contains the information about a 
 * room in the Adventure Game.
 */
public class Room implements Serializable {

    @Serial
    private static final long serialVersionUID = -6731425071213512513L;

    private final String adventureName;

    /**
     * The number of the room.
     */
    private int roomNumber;

    /**
     * The name of the room.
     */
    private String roomName;

    /**
     * The description of the room.
     */
    private String roomDescription;

    /**
     * The passage table for the room.
     */
    private PassageTable motionTable = new PassageTable();

    /**
     * The list of objects in the room.
     */
    public ArrayList<AdventureObject> objectsInRoom = new ArrayList<AdventureObject>();

    /**
     * A boolean to store if the room has been visited or not
     */
    private boolean isVisited;
    private boolean hasNPC;
    private NPC npc = null;

    /**
     * Tracks secret rooms
     */
    private boolean secretRoom;

    /**
     * This attribute tracks whether any secret room has been found.
     */
    private static boolean secretRooomFound = false;

    /**
     * The list of secret rooms by room number.
     */
    public static ArrayList<String> secretRooms = new ArrayList<>();


    /**
     * AdvGameRoom constructor.
     *
     * @param roomName: The name of the room.
     * @param roomNumber: The number of the room.
     * @param roomDescription: The description of the room.
     */
    public Room(String roomName, int roomNumber, String roomDescription, String adventureName){
        this.roomName = roomName;
        this.roomNumber = roomNumber;
        this.roomDescription = roomDescription;
        this.adventureName = adventureName;
        this.isVisited = false;
        this.hasNPC = false;
        this.secretRoom = false;
    }


    /**
     * Returns a comma delimited list of every
     * object's description that is in the given room,
     * e.g. "a can of tuna, a beagle, a lamp".
     *
     * @return delimited string of object descriptions
     */
    public String getObjectString() {
        StringBuilder x = new StringBuilder();
        for (AdventureObject a : objectsInRoom) {
            x.append(a.getDescription());
            x.append(",");
        }
        if (!x.isEmpty()) {
            x.delete(x.length() - 1, x.length());
        }
        return x.toString();
    }

    /**
     * Returns a comma delimited list of every
     * move that is possible from the given room,
     * e.g. "DOWN, UP, NORTH, SOUTH".
     *
     * @return delimited string of possible moves
     */
    public String getCommands() {
        StringBuilder x = new StringBuilder();
        for (Passage a : motionTable.passageTable) {
            if (Room.secretRooms.contains(Integer.toString(a.getDestinationRoom()))) { // Secret Passage
                continue; // Skip the rest of this iteration
            }
            x.append(a.getDirection());
            x.append(",");
        }
        if (!x.isEmpty()) {
            x.delete(x.length() - 1, x.length());
        }
        return x.toString();
    }

    /**
     * This method adds a game object to the room.
     *
     * @param object to be added to the room.
     */
    public void addGameObject(AdventureObject object){
        this.objectsInRoom.add(object);
    }

    /**
     * This method removes a game object from the room.
     *
     * @param object to be removed from the room.
     */
    public void removeGameObject(AdventureObject object){
        this.objectsInRoom.remove(object);
    }

    /**
     * This method checks if an object is in the room.
     *
     * @param objectName Name of the object to be checked.
     * @return true if the object is present in the room, false otherwise.
     */
    public boolean checkIfObjectInRoom(String objectName){
        for(int i = 0; i<objectsInRoom.size();i++){
            if(this.objectsInRoom.get(i).getName().equals(objectName)) return true;
        }
        return false;
    }

    /**
     * Sets the visit status of the room to true.
     */
    public void visit(){
        isVisited = true;
        if (this.isSecretRoom()) { // Secret room found
            this.secretRoom = false; // Room is no longer secret
            Room.secretRooms.remove(Integer.toString(this.roomNumber)); // Update list of secret rooms
            Room.secretRooomFound = true; // Perceptive achievement unlocked

        }
    }

    /**
     * Getter for returning an AdventureObject with a given name
     *
     * @param objectName: Object name to find in the room
     * @return: AdventureObject
     */
    public AdventureObject getObject(String objectName){
        for(int i = 0; i<objectsInRoom.size();i++){
            if(this.objectsInRoom.get(i).getName().equals(objectName)) return this.objectsInRoom.get(i);
        }
        return null;
    }

    /**
     * Getter method for the number attribute.
     *
     * @return: number of the room
     */
    public int getRoomNumber(){
        return this.roomNumber;
    }

    /**
     * Getter method for the description attribute.
     *
     * @return: description of the room
     */
    public String getRoomDescription(){
        return this.roomDescription.replace("\n", " ");
    }


    /**
     * Getter method for the name attribute.
     *
     * @return: name of the room
     */
    public String getRoomName(){
        return this.roomName;
    }

    /**
     * Getter method for the visit attribute.
     *
     * @return: visit status of the room
     */
    public boolean getVisited(){
        return this.isVisited;
    }

    /**
     * Getter method for the motionTable attribute.
     *
     * @return: motion table of the room
     */
    public PassageTable getMotionTable(){
        return this.motionTable;
    }

    public void setNPC(NPC npc) {
        this.hasNPC = true;
        this.npc = npc;
    }

    public NPC getNPC() {
        return this.npc;
    }

    /**
     * This method checks whether the current room is a secret room.
     *
     * @return whether the current room is a secret room.
     */
    public boolean isSecretRoom() {
        return this.secretRoom;
    }

    /**
     * This method declares the current room as a secret room.
     */
    public void setSecretRoom() {
        this.secretRoom = true;
    }

    /**
     * This method checks if the Perceptive achievement is unlocked. This achievement unlocks whenever any secret room
     * is visited at least once.
     *
     * @return whether the achievement is unlocked.
     */
    public static boolean isPerceptiveAchievementUnlocked() {
        return Room.secretRooomFound;
    }

    /**
     * This method locks the Perceptive achievement.
     */
    public static void lockPerceptiveAchievement() {
        Room.secretRooomFound = false;
    }

    /**
     * This method unlocks the Perceptive achievement.
     */
    public static void unlockPerceptiveAchievement() {
        Room.secretRooomFound = true;
    }

}
