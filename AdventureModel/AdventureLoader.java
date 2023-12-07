package AdventureModel;

import AdventureModel.Moods.Friendly;
import AdventureModel.Moods.Hostile;
import AdventureModel.Moods.Mood;
import AdventureModel.Moods.Neutral;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class AdventureLoader. Loads an adventure from files.
 */
public class AdventureLoader {

    private AdventureGame game; //the game to return
    private String adventureName; //the name of the adventure

    /**
     * Adventure Loader Constructor
     * __________________________
     * Initializes attributes
     * @param game the game that is loaded
     * @param directoryName the directory in which game files live
     */
    public AdventureLoader(AdventureGame game, String directoryName) {
        this.game = game;
        this.adventureName = directoryName;
    }

     /**
     * Load game from directory
     */
    public void loadGame() throws IOException {
        parseRooms();
        parseSecretRooms(); // Secret rooms
        parseObjects();
        parseSecretObjects(); // Secret objects
        parseSynonyms();
        parseNPCs();
        Mood.loadSynonyms(this.adventureName); // Load mood-synonyms
        this.game.setHelpText(parseOtherFile("help"));
    }

     /**
     * Parse Rooms File
     */
    private void parseRooms() throws IOException {

        int roomNumber;

        String roomFileName = this.adventureName + "/rooms.txt";
        BufferedReader buff = new BufferedReader(new FileReader(roomFileName));

        while (buff.ready()) {

            String currRoom = buff.readLine(); // first line is the number of a room

            roomNumber = Integer.parseInt(currRoom); //current room number

            // now need to get room name
            String roomName = buff.readLine();

            // now we need to get the description
            String roomDescription = "";
            String line = buff.readLine();
            while (!line.equals("-----")) {
                roomDescription += line + "\n";
                line = buff.readLine();
            }
            roomDescription += "\n";

            // now we make the room object
            Room room = new Room(roomName, roomNumber, roomDescription, adventureName);

            // now we make the motion table
            line = buff.readLine(); // reads the line after "-----"
            while (line != null && !line.equals("")) {
                String[] part = line.split(" \s+"); // have to use regex \\s+ as we don't know how many spaces are between the direction and the room number
                String direction = part[0];
                String dest = part[1];
                if (dest.contains("/")) {
                    String[] blockedPath = dest.split("/");
                    String dest_part = blockedPath[0];
                    String object = blockedPath[1];
                    Passage entry = new Passage(direction, dest_part, object);
                    room.getMotionTable().addDirection(entry);
                } else if (dest.contains("*")) {
                    String[] lockedPath = dest.split("\\*");
                    String dest_part = lockedPath[0];
                    ArrayList<String> keys = new ArrayList<>();
                    for (int i = 1; i < lockedPath.length; i++) {
                        keys.add(lockedPath[i]);
                    }

                    Passage entry = new Passage(direction, dest_part, keys, true);
                    room.getMotionTable().addDirection(entry);

                } else {
                    Passage entry = new Passage(direction, dest);
                    room.getMotionTable().addDirection(entry);
                }
                line = buff.readLine();
            }
            this.game.getRooms().put(room.getRoomNumber(), room);
        }

    }

    /**
     * Parse Secret Rooms File
     */
    private void parseSecretRooms() throws IOException { // Secret rooms

        int roomNumber;

        String roomFileName = this.adventureName + "/rooms_secret.txt";
        BufferedReader buff = new BufferedReader(new FileReader(roomFileName));
        boolean emptySecretRooms = Room.secretRooms.isEmpty();

        while (buff.ready()) {

            String currRoom = buff.readLine(); // first line is the number of a room

            roomNumber = Integer.parseInt(currRoom); //current room number

            // now need to get room name
            String roomName = buff.readLine();

            // now we need to get the description
            String roomDescription = "";
            String line = buff.readLine();
            while (!line.equals("-----")) {
                roomDescription += line + "\n";
                line = buff.readLine();
            }
            roomDescription += "\n";

            // now we make the room object
            Room room = new Room(roomName, roomNumber, roomDescription, adventureName);

            room.setSecretRoom(); // Secret rooms
            if (emptySecretRooms) {
                Room.secretRooms.add(Integer.toString(roomNumber)); // Secret room number
            }


            // now we make the motion table
            line = buff.readLine(); // reads the line after "-----"
            while (line != null && !line.equals("")) {
                String[] part = line.split(" \s+"); // have to use regex \\s+ as we don't know how many spaces are between the direction and the room number
                String direction = part[0];
                String dest = part[1];
                if (dest.contains("/")) {
                    String[] blockedPath = dest.split("/");
                    String dest_part = blockedPath[0];
                    String object = blockedPath[1];
                    Passage entry = new Passage(direction, dest_part, object);
                    room.getMotionTable().addDirection(entry);
                } else {
                    Passage entry = new Passage(direction, dest);
                    room.getMotionTable().addDirection(entry);
                }
                line = buff.readLine();
            }
            this.game.getRooms().put(room.getRoomNumber(), room);
        }

    }

     /**
     * Parse Objects File
     */
    public void parseObjects() throws IOException {

        String objectFileName = this.adventureName + "/objects.txt";
        BufferedReader buff = new BufferedReader(new FileReader(objectFileName));

        while (buff.ready()) {
            String objectName = buff.readLine();
            String objectDescription = buff.readLine();
            String objectLocation = buff.readLine();
            String separator = buff.readLine();
            if (separator != null && !separator.isEmpty())
                System.out.println("Formatting Error!");
            int i = Integer.parseInt(objectLocation);
            Room location = this.game.getRooms().get(i);
            AdventureObject object = new AdventureObject(objectName, objectDescription, location);
            object.setOpinionReward(0);
            location.addGameObject(object);
        }

    }

    /**
     * Parse Secret Objects File
     */
    public void parseSecretObjects() throws IOException { // Secret objects

        String objectFileName = this.adventureName + "/objects_gifts.txt";
        BufferedReader buff = new BufferedReader(new FileReader(objectFileName));

        while (buff.ready()) {
            String objectName = buff.readLine();
            String objectDescription = buff.readLine();
            String objectLocation = buff.readLine();
            String separator = buff.readLine();
            if (separator != null && !separator.isEmpty())
                System.out.println("Formatting Error!");
            int i = Integer.parseInt(objectLocation);
            Room location = this.game.getRooms().get(i);
            AdventureObject object = new AdventureObject(objectName, objectDescription, location);
            object.setGift(); // Giftable objects
            if (objectName.equals("CAKE")) {
                object.setOpinionReward(100);
            } else if (objectName.equals("ROTTEN_CAKE")) {
                object.setOpinionReward(-100);
            } else if (objectName.equals("POOP")) {
                object.setOpinionReward(-300);
            }
            location.addGameObject(object);
        }

    }



    private void parseNPCs() throws IOException {
        BufferedReader buff = new BufferedReader(new FileReader(this.adventureName + "/NPCs.txt"));
        while (buff.ready()) {
            String x = buff.readLine();
            int roomNumber = Integer.parseInt(x);
            String line = buff.readLine();
            String name = buff.readLine();

            Room r = this.game.getRooms().get(roomNumber);
            if (line.endsWith("HUMAN")) {
                Human h = new Human(name, r);
                r.setNPC(h);
                String openingText = buff.readLine();
                String[] options = buff.readLine().split(", ");
                h.setInteraction(openingText, options);
                Mood mood = null;
                if (line.startsWith("FRIENDLY")) {
                    mood = new Friendly();
                }
                else if (line.startsWith("NEUTRAL")) {
                    mood = new Neutral();
                }
                else {
                    mood = new Hostile();
                }
                h.setMood(mood);
                h.updateText();
            } else if (line.equals("TROLL")) {
                Troll t = new Troll(name, r);
                r.setNPC(t);
                String openingText = buff.readLine();
                String[] options = buff.readLine().split(", ");
                String correct = buff.readLine();
                String right = buff.readLine();
                String wrong = buff.readLine();
                t.setInteraction(openingText, options, correct, right, wrong);
            }
            buff.readLine();
        }
    }

     /**
     * Parse Synonyms File
     */
    public void parseSynonyms() throws IOException {
        String synonymsFileName = this.adventureName + "/synonyms.txt";
        BufferedReader buff = new BufferedReader(new FileReader(synonymsFileName));
        String line = buff.readLine();
        while(line != null){
            String[] commandAndSynonym = line.split("=");
            String command1 = commandAndSynonym[0];
            String command2 = commandAndSynonym[1];
            this.game.getSynonyms().put(command1,command2);
            line = buff.readLine();
        }

    }

    /**
     * Parse Files other than Rooms, Objects and Synonyms
     *
     * @param fileName the file to parse
     */
    public String parseOtherFile(String fileName) throws IOException {
        String text = "";
        fileName = this.adventureName + "/" + fileName + ".txt";
        BufferedReader buff = new BufferedReader(new FileReader(fileName));
        String line = buff.readLine();
        while (line != null) { // while not EOF
            text += line+"\n";
            line = buff.readLine();
        }
        return text;
    }

}
