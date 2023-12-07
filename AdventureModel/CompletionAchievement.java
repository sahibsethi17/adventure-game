package AdventureModel;
/**
 * Class of Achievement for finishing the game
 */
public class CompletionAchievement extends Achievement{
    /**
     * CompletionAchievement constructor
     * __________________________
     * Initializes attributes
     */
    public CompletionAchievement() {
        super();
        this.category = "completion";
    }

    /**
     * Updates the status of the achievement object. This method is only meant to be called when the game
     * is completed so no condition must be checked.
    */
    @Override
    public boolean updateStatus(AdventureGame model) {
        this.achieved = true;
        return true;
    }

    @Override
    public String getAchievement(){
        return "Completed the game";
    }

    @Override
    public String getFileString() {
        if (this.achieved) {
            return "completion 1\n";
        } else {
            return "completion 0\n";
        }
    }
}