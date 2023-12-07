package AdventureModel;

/**
 * This abstract class provides a template for different kinds of achievements.
 */
public abstract class Achievement {
    /**
     * Keeps track of whether the condition for the achievement has been fulfilled by the user
     */
    boolean achieved;
    /**
     * The kind of Achievement class of the object
     */
    String category;

    /**
     * Achievement constructor
     * __________________________
     * Initializes attributes
     */
    public Achievement() {
        this.achieved = false;
    }

    /**
     * Updates the the status of the achieved attribute if the achievement's condition is fulfilled.
     * @param model the game that is loaded
     * @return a boolean to represent whether status was changed or not
     */
    abstract boolean updateStatus(AdventureGame model);
    /**
     * @return  a string description of the achievement which can be displayed to the user.
     */
    abstract String getAchievement();


    /**
     * Updates the the status of the achieved attribute ri4
     * @param status
     */
    public void setStatus(int status) {
        if (status==0) {
            this.achieved = false;
        } else if (status==1){
            this.achieved = true;
        } else {
            System.out.println("Invalid Status Change");
        }
    }

    /**
     * @return a String which indicates whether the status of the achieved attribute
     */
    public String getStatusString() {
        if (this.achieved) {
            return "Completed";
        } else {
            return "Not Completed";
        }
    }
    /**
     * @return  a string representation of the achievement and whether it has been completed that can be
     * written to a file and read later to keep track of the completion status.
     */
    abstract String getFileString();
}
