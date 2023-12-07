package AdventureModel;

public class SecretAchievement extends Achievement{

    public SecretAchievement() {
        super();
        this.category = "secret";
    }

    @Override
    public boolean updateStatus(AdventureGame model) {
        if (Room.isPerceptiveAchievementUnlocked()) {
            this.achieved = true;
            return true;
        }
        return false;
    }

    @Override
    public String getAchievement(){
        return "Found a secret room";
    }

    @Override
    public String getFileString() {
        if (this.achieved) {
            return "secret 1\n";
        } else {
            return "secret 0\n";
        }
    }
}
