package AdventureModel;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class VisitedRoomsTest {
    @Test
    public void VisitedStringTest() throws IOException {
        AchievementList.getInstance();
        AdventureGame model = new AdventureGame("TinyGame");
        AchievementList.populateList(model);
        AchievementList.populateCategories();
        assertEquals("", model.getVisitedRoomsString());
        model.player.getCurrentRoom().visit();
        String testString = "Outside building\n";
        assertEquals(testString, model.getVisitedRoomsString());

        model.movePlayer("IN");
        model.player.getCurrentRoom().visit();
        testString += "Inside building\n";
        assertEquals(testString, model.getVisitedRoomsString());

        model.movePlayer("STAIRS");
        model.player.getCurrentRoom().visit();
        testString += "Kitchen\n";
        assertEquals(testString, model.getVisitedRoomsString());

    }
}
