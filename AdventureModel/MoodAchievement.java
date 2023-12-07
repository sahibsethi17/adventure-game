package AdventureModel;

import AdventureModel.Moods.Mood;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.List;

public class MoodAchievement extends Achievement{

    /**
     * The type of MoodAchievement the object is.
     */
    String type;
    /**
     * A list of all possible types of MoodAchievement.
     */
    final List<String> types = Arrays.asList("sadistic", "charismatic", "swing");

    /**
     * MoodAchievement Constructor
     * __________________________
     * Initializes attributes
     * @param type the type of MoodAchievement being instantiated.
     * @throws IllegalArgumentException if the type parameter isn't one of the possible types in the types attribute
     */
    public MoodAchievement(String type) {
        super();
        String ltype = type.toLowerCase();
        this.category = "mood";
        if (!types.contains(ltype)) {
            throw new IllegalArgumentException();
        }
        this.type = ltype;
    }
    @Override
    public boolean updateStatus(AdventureGame model) {
        if (Objects.equals(this.type, "charismatic")) {
            if (Mood.isCharismaticAchievementUnlocked()) {
                this.achieved = true;
                return true;
            }
        }
        if (Objects.equals(this.type, "sadistic")) {
            if (Mood.isSadisticAchievementUnlocked()) {
                this.achieved = true;
                return true;
            }
        }
        if (Objects.equals(this.type, "swing")) {
            if (Mood.isMoodSwingAchievementUnlocked()) {
                this.achieved = true;
                return true;
            }
        }
        return false;
    }
    @Override
    public String getAchievement() {
        if (Objects.equals(this.type, "charismatic")) {
            return "Charmed an NPC";
        }
        if (Objects.equals(this.type, "sadistic")) {
            return "Made an NPC hate you";
        }
        return "Made an NPC change their mind about you";
    }
    @Override
    public String getFileString() {
        if (this.achieved) {
            return "mood " + this.type + " 1\n";
        } else {
            return "mood " + this.type + " 0\n";
        }
    }
}
