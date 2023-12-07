package AdventureModel;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class AchievementList {
    /**
     * The single instance of AchievementList according to the Singleton pattern.
     */
    private static final AchievementList aInstance = new AchievementList();
    /**
     * A Hashmap of all the achievements mapped to their category.
     */
    public HashMap<String, ArrayList<Achievement>> aCategories;
    /**
     * List containing all achievements for the game
     */
    public ArrayList<Achievement> aList;
    private String scoreString;
    /**
     * AchievementList Constructor
     * __________________________
     * Initializes attributes, private according to the Singleton pattern.
     */
    private AchievementList(){
        this.aCategories = new HashMap<>();
        this.aList = new ArrayList<>();
    };
    /**
     * @return The single instance of AchievementList according to the Singleton pattern.
     */
    public static AchievementList getInstance() {
        return aInstance;
    }

    /**
     * Adds the parameter to the list of achievements.
     * @param a THe achievement to add to the list of achievements.
     */
    public void putList(Achievement a) {
        this.aList.add(a);
        populateCategories();
    }

    /**
     * Resets the aList and aCategories attributes, reads the achievements.txt file for the current game and
     * adds the achievements inside to aList.
     * @param model The AdventureGame object being used currently
     */
    public static void populateList(AdventureGame model) throws IOException {
        aInstance.aList = new ArrayList<>();
        aInstance.aCategories = new HashMap<>();
        FileReader fr = new FileReader(model.getDirectoryName() + "/achievements.txt");
        BufferedReader br = new BufferedReader(fr);
        String cline;
        Achievement a;
        cline = br.readLine();
        String[] sline;
        while (cline != null) {
            sline = cline.trim().split("\\s+");
            if (sline.length == 2) {
                if (sline[0].toLowerCase().equals("completion")) {
                    a = new CompletionAchievement();
                    a.setStatus(Integer.parseInt(sline[1]));
                    aInstance.aList.add(a);
                }
                if (sline[0].toLowerCase().equals("secret")) {
                    a = new SecretAchievement();
                    a.setStatus(Integer.parseInt(sline[1]));
                    aInstance.aList.add(a);
                }
            }
            if (sline.length == 3) {
                if (sline[0].toLowerCase().equals("mood")) {
                    a = new MoodAchievement(sline[1]);
                    a.setStatus(Integer.parseInt(sline[2]));
                    aInstance.aList.add(a);
                }
            }
            cline = br.readLine();
        }
        br.close();
        AchievementList.updateScoreString();
    }

    /**
     * Writes all the achievements contained in the Achievement List and their completion status to the
     * achievements file in the game's directory, in order to save them.
     * @param model the AdventureGame object currently being used
     */
    public static void saveAchievements(AdventureGame model) {
        try {
            FileWriter fr = new FileWriter(model.getDirectoryName() + "/achievements.txt");
            for (Achievement a: aInstance.aList) {
                String fs = a.getFileString();
                fr.write(fs);
            }
            fr.close();
        } catch (IOException ie) {
            System.out.println("Error in Saving Achievements");
        }
    }
    /**
     * Populates the aCategories hashmap which maps achievements to their category.
     */
    public static void populateCategories() {
        ArrayList<Achievement> clist;
        for (Achievement a: aInstance.aList) {
            if (aInstance.aCategories.get(a.category) == null) {
                clist = new ArrayList<>();
                clist.add(a);
                aInstance.aCategories.put(a.category.toLowerCase(), clist);
            } else {
                clist = aInstance.aCategories.get(a.category);
                clist.add(a);
                aInstance.aCategories.put(a.category, clist);
            }
        }
    }

    /**
     * Updates the status of all achievements in a given category.
     * @param category the category being checked
     * @param model the AdventureGame object currently being used
     */
    public void checkCategory(String category, AdventureGame model) {
        for (Achievement a: aInstance.aCategories.get(category)) {
            if (!a.achieved) {
                a.updateStatus(model);
            }
        }
    }

    /**
     * @return an ArrayList of strings containing a description of all achievements and their completion status
     * and the scoreString so that they can be displayed to the user.
     */
    public ArrayList<String> getStringList() {
        ArrayList<String> retlist = new ArrayList<>();
        for (Achievement a: aInstance.aList) {
            retlist.add(a.getAchievement() + "---" + a.getStatusString());
        }
        retlist.add(aInstance.scoreString);
        return  retlist;
    }
    /**
     * @return the score of the user from 0 to 3 based on the percentage of achievements completed
     */
    public int getScore() {
        int total = aInstance.aList.size();
        double completed = 0.0;
        for (Achievement a: aInstance.aList) {
            if (a.achieved) {
                completed += 1.0;
            }
        }
        if (completed/total <= 0.25) {
            return 0;
        }
        if (completed/total > 0.25 && completed/total <= 0.50) {
            return 1;
        }
        if (completed/total > 0.50 && completed/total != 1.0) {
            return 2;
        }
        if (completed/total == 1.0) {
            return 3;
        }
        return 0;
    }

    /**
     * Updates the AchievementList object's scoreString attribute based on the score from getScore.
     */
    public static void updateScoreString() {
        int score = aInstance.getScore();
        if (score==0) {
            aInstance.scoreString = "You are a Novice!";
            return;
        }
        if (score==1) {
            aInstance.scoreString = "You are an Intermediate!";
            return;
        }
        if (score==2) {
            aInstance.scoreString = "You are an Expert!";
            return;
        }
        if (score==3) {
            aInstance.scoreString = "You are a Master!";
            return;
        }
    }

    /**
     * @return the AchievementList object's scoreString attribute which is based on the score from getScore
     */
    public String getScoreString() {
        return aInstance.scoreString;
    }


}
