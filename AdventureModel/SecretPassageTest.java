package AdventureModel;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SecretPassageTest {

    @Test
    void commandsTest() {

        AdventureGame game = new AdventureGame("TinyGame");
        String commands;

        // Tracks visited secret passages
        boolean kitchenPassageCommand = false;
        boolean pantryPassageCommand = false;
        boolean cellarPassageCommand = false;

        assertEquals(1, game.player.getCurrentRoom().getRoomNumber()); // Outside building

        game.movePlayer("IN");
        game.player.getCurrentRoom().visit();
        assertEquals(3, game.player.getCurrentRoom().getRoomNumber()); // Inside Building
        commands = game.player.getCurrentRoom().getCommands();
        for (String command : commands.split(",")) { // Check secret passages
            if (command.equals("STAIRS")) {
                kitchenPassageCommand = true;
            } else if (command.equals("TRAPDOOR")) {
                cellarPassageCommand = true;
            }
        }
        assertFalse(kitchenPassageCommand); // Kitchen not visited
        assertFalse(cellarPassageCommand); // Cellar not visited

        game.movePlayer("STAIRS");
        game.player.getCurrentRoom().visit();
        assertEquals(11, game.player.getCurrentRoom().getRoomNumber()); // Kitchen
        commands = game.player.getCurrentRoom().getCommands();
        for (String command : commands.split(",")) { // Check secret passages
            if (command.equals("DOOR")) {
                pantryPassageCommand = true;
                break;
            }
        }
        assertFalse(pantryPassageCommand); // Pantry not visited

        game.movePlayer("STAIRS");
        game.player.getCurrentRoom().visit();
        assertEquals(3, game.player.getCurrentRoom().getRoomNumber()); // Inside Building
        commands = game.player.getCurrentRoom().getCommands();
        for (String command : commands.split(",")) { // Check secret passages
            if (command.equals("STAIRS")) {
                kitchenPassageCommand = true;
            } else if (command.equals("TRAPDOOR")) {
                cellarPassageCommand = true;
            }
        }
        assertTrue(kitchenPassageCommand); // Kitchen was visited
        assertFalse(cellarPassageCommand); // Cellar not visited

        game.movePlayer("STAIRS");
        game.player.getCurrentRoom().visit();
        assertEquals(11, game.player.getCurrentRoom().getRoomNumber()); // Kitchen

        game.movePlayer("DOOR");
        game.player.getCurrentRoom().visit();
        assertEquals(12, game.player.getCurrentRoom().getRoomNumber()); // Pantry

        game.movePlayer("DOOR");
        game.player.getCurrentRoom().visit();
        assertEquals(11, game.player.getCurrentRoom().getRoomNumber()); // Kitchen
        commands = game.player.getCurrentRoom().getCommands();
        for (String command : commands.split(",")) { // Check secret passages
            if (command.equals("DOOR")) {
                pantryPassageCommand = true;
                break;
            }
        }
        assertTrue(pantryPassageCommand); // Pantry was visited

        game.movePlayer("STAIRS");
        game.player.getCurrentRoom().visit();
        assertEquals(3, game.player.getCurrentRoom().getRoomNumber()); // Inside Building

        game.movePlayer("TRAPDOOR");
        game.player.getCurrentRoom().visit();
        assertEquals(13, game.player.getCurrentRoom().getRoomNumber()); // Cellar

        game.movePlayer("UP");
        game.player.getCurrentRoom().visit();
        assertEquals(3, game.player.getCurrentRoom().getRoomNumber()); // Inside Building
        commands = game.player.getCurrentRoom().getCommands();
        for (String command : commands.split(",")) { // Check secret passages
            if (command.equals("TRAPDOOR")) {
                cellarPassageCommand = true;
                break;
            }
        }
        assertTrue(kitchenPassageCommand); // Kitchen was visited
        assertTrue(cellarPassageCommand); // Cellar was visited

        Room.lockPerceptiveAchievement();
        Room.secretRooms = new ArrayList<>();

    }

    @Test
    void perceptiveAchievementTest() throws IOException {

        AdventureGame game = new AdventureGame("TinyGame");

        assertFalse(Room.isPerceptiveAchievementUnlocked());

        // Unlock achievement
        Room.unlockPerceptiveAchievement();
        assertTrue(Room.isPerceptiveAchievementUnlocked());

        // Lock achievement
        Room.lockPerceptiveAchievement();
        assertFalse(Room.isPerceptiveAchievementUnlocked());

        assertEquals(1, game.player.getCurrentRoom().getRoomNumber()); // Outside building

        AchievementList.getInstance().populateList(game);
        AchievementList.getInstance().populateCategories();
        game.movePlayer("IN");
        game.player.getCurrentRoom().visit();
        assertEquals(3, game.player.getCurrentRoom().getRoomNumber()); // Inside Building
        assertFalse(Room.isPerceptiveAchievementUnlocked());

        game.movePlayer("STAIRS");
        game.player.getCurrentRoom().visit();
        assertEquals(11, game.player.getCurrentRoom().getRoomNumber()); // Kitchen
        assertTrue(Room.isPerceptiveAchievementUnlocked());

        game.movePlayer("STAIRS");
        game.player.getCurrentRoom().visit();
        assertEquals(3, game.player.getCurrentRoom().getRoomNumber()); // Inside Building
        Room.lockPerceptiveAchievement();

//        game.movePlayer("TRAPDOOR");
//        game.player.getCurrentRoom().visit();
//        assertEquals(13, game.player.getCurrentRoom().getRoomNumber()); // Cellar
//        assertTrue(Room.isPerceptiveAchievementUnlocked());
//        Room.lockPerceptiveAchievement();
//        Room.secretRooms = new ArrayList<>();

    }
    @Test
    void SecretTest(){
        AdventureGame game = new AdventureGame("TinyGame");
        assertEquals(1, game.player.getCurrentRoom().getRoomNumber()); // Outside building
        game.movePlayer("IN");
        game.player.getCurrentRoom().visit();
        assertEquals(3, game.player.getCurrentRoom().getRoomNumber()); // Inside Building
        game.movePlayer("STAIRS");
        game.player.getCurrentRoom().visit();
        assertEquals(11, game.player.getCurrentRoom().getRoomNumber()); // Kitchen
        game.movePlayer("STAIRS");
        game.player.getCurrentRoom().visit();
        assertEquals(3, game.player.getCurrentRoom().getRoomNumber()); // Inside Building
        game.movePlayer("TRAPDOOR");
        assertEquals(3, game.player.getCurrentRoom().getRoomNumber()); // Inside Building
        game.movePlayer("OUT");
        game.interpretAction("TAKE BIRD");
        game.movePlayer("WEST");
        game.interpretAction("TAKE CHEST");
        game.movePlayer("EAST");
        game.movePlayer("IN");
        game.movePlayer("TRAPDOOR");
        assertEquals(13, game.player.getCurrentRoom().getRoomNumber()); // Cellar



    }

}
