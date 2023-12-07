package AdventureModel.Moods;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public interface Mood {

    /**
     * The name of the file that contains friendly synonyms.
     */
    String FRIENDLY_FILE = "synonyms_friendly.txt";

    /**
     * The name of the file that contains neutral synonyms.
     */
    String NEUTRAL_FILE = "synonyms_neutral.txt";

    /**
     * The name of the file that contains hostile synonyms.
     */
    String HOSTILE_FILE = "synonyms_hostile.txt";

    /**
     * The threshold of opinion points required to advance to a better mood.
     */
    int PROMOTION_THRESHOLD = 100;

    /**
     * The threshold of opinion points required to regress to a worse mood.
     */
    int DEMOTION_THRESHOLD = 0;

    /**
     * This method paraphrases dialogue using appropriate synonyms.
     *
     * @param dialogue the original dialogue
     * @return the improved dialogue
     */
    String paraphrase(String dialogue);

    /**
     * This method defines how different moods exhibit generosity. Friendly moods are always generous; neutral moods
     * are generous when they have sufficient opinion points; hostile moods are never generous.
     *
     * @return true if a reward should be given, false otherwise.
     */
    boolean isGenerous();

    /**
     * This method updates the current mood if there are sufficient opinion points. Moods improve when they have more
     * than one hundred opinion points; however, worsen when there are less than zero opinion points.
     *
     * @return the updated mood.
     */
    Mood update();

    /**
     * This method improves the current mood if possible. Any extra opinion points are carried over.
     *
     * @return the corresponding improved mood.
     */
    Mood promote();

    /**
     * This method worsens the current mood if possible. Any extra opinion points are carried over.
     *
     * @return the corresponding worsened mood.
     */
    Mood demote();

    /**
     * This method increases opinion points.
     *
     * @param opinion the increase in opinion points.
     */
    void increaseOpinion(int opinion);

    /**
     * This method decreases opinion points.
     *
     * @param opinion the decrease in opinion points.
     */
    void decreaseOpinion(int opinion);

    /**
     * This method sets the opinion points.
     *
     * @param opinion the desired opinion points.
     */
    void setOpinion(int opinion);

    /**
     * This method gets the opinion points.
     *
     * @return the current opinion points.
     */
    int getOpinion();

    /**
     * This method checks if the Charismatic achievement is unlocked. This achievement unlocks whenever any friendly
     * entity ever exceeds <code>PROMOTION_THRESHOLD</code> opinion points.
     *
     * @return true if the achievement is unlocked.
     */
    static boolean isCharismaticAchievementUnlocked() {
        return Friendly.ecstatic;
    }

    /**
     * This method checks if the Sadistic achievement is unlocked. This achievement unlocks whenever any hostile entity
     * ever has less than negative <code>PROMOTION_THRESHOLD</code> opinion points.
     *
     * @return true if the achievement is unlocked.
     */
    static boolean isSadisticAchievementUnlocked() {
        return Hostile.livid;
    }

    /**
     * This method checks if the Mood Swing achievement is unlocked. This achievement unlocks whenever a single
     * interaction converts entities from friendly to hostile or vice versa.
     *
     * @return true if the achievement is unlocked.
     */
    static boolean isMoodSwingAchievementUnlocked() {
        return Friendly.hostileMoodSwing || Hostile.friendlyMoodSwing;
    }

    /**
     * This method locks the Charismatic achievement.
     */
    static void lockCharismaticAchievement() {
        Friendly.ecstatic = false;
    }

    /**
     * This method locks the Sadistic achievement.
     */
    static void lockSadisticAchievement() {
        Hostile.livid = false;
    }

    /**
     * This method locks the Mood Swing achievement.
     */
    static void lockMoodSwingAchievement() {
        Friendly.hostileMoodSwing = false;
        Hostile.friendlyMoodSwing = false;
    }

    /**
     * This method unlocks the Charismatic achievement.
     */
    static void unlockCharismaticAchievement() {
        Friendly.ecstatic = true;
    }

    /**
     * This method unlocks the Sadistic achievement.
     */
    static void unlockSadisticAchievement() {
        Hostile.livid = true;
    }

    /**
     * This method unlocks the Mood Swing achievement.
     */
    static void unlockMoodSwingAchievement() {
        Friendly.hostileMoodSwing = true;
        Hostile.friendlyMoodSwing = true;
    }

    /**
     * This method parses all mood-synonym files, then loads them into their respective classes. The AdventureLoader
     * uses this method within its loadGame() method.
     *
     * @param adventureName the name of the game.
     */
    static void loadSynonyms(String adventureName) throws IOException {

        Friendly.friendlySynonyms = Mood.parseSynonyms(adventureName, FRIENDLY_FILE);
        Neutral.neutralSynonyms = Mood.parseSynonyms(adventureName, NEUTRAL_FILE);
        Hostile.hostileSynonyms = Mood.parseSynonyms(adventureName, HOSTILE_FILE);
    }

    /**
     * This method parses the synonyms of every word in a file. Each line in this file contains several
     * words that are each separated by an equals sign. These words are all synonyms of the first word in each line.
     * Note that a word is a synonym of itself.
     *
     * @param adventureName the name of the game.
     * @param fileName the file name.
     * @return a HashMap of words to an array of its synonyms.
     */
    private static HashMap<String, String[]> parseSynonyms(String adventureName, String fileName) throws IOException {

        HashMap<String, String[]> wordsToSynonyms= new HashMap<>();

        String directory = adventureName + "/" + fileName; // File directory
        BufferedReader buff = new BufferedReader(new FileReader(directory)); // Open file
        String line = buff.readLine(); // First line in the file

        while(line != null){

            String[] synonyms; // Synonyms of first word
            String word; // First word

            synonyms = Mood.strip(line.split("([ ]*=[ ]*|^[ ]+|[ ]+$)"));

            if (synonyms.length <= 1) { // Invalid syntax
                line = buff.readLine(); // Next line
                continue; // Skip iteration
            }

            word = synonyms[0];
            wordsToSynonyms.put(word, Arrays.copyOfRange(synonyms, 1, synonyms.length)); // Update hashmap
            line = buff.readLine(); // Next line
        }

        buff.close(); // Close file
        return wordsToSynonyms;

    }

    /**
     * This method paraphrases dialogue using appropriate synonyms.
     *
     * @param dialogue the original dialogue.
     * @param synonyms the map of words to appropriate synonyms.
     * @return the improved dialogue.
     */
    static String paraphraseDialogue(String dialogue, HashMap<String , String[]> synonyms) {

        if (synonyms.isEmpty() || dialogue.isEmpty()) { // No synonyms exist
            return dialogue;
        }

        // Deconstruct dialogue
        String[] words = Mood.strip(dialogue.split("[^a-zA-Z]+")); // Collect all words
        String[] punctuation = Mood.strip(dialogue.split("[a-zA-Z]+")); // Collect all non-words

        // Paraphrase words
        for (int i = 0; i < words.length; i++) {
            words[i] = Mood.paraphraseWord(words[i], synonyms);
        }

        // Reconstruct dialogue
        StringBuilder result = new StringBuilder();
        int wordLength = words.length;
        int punctuationLength = punctuation.length;

        // Note that punctuation always exists between words.
        if (Character.isAlphabetic(dialogue.charAt(0))) { // Dialogue begins with word
            if (wordLength > punctuationLength) { // Dialogue ends with word
                result.append(Mood.combineText(words, punctuation, punctuationLength));
                result.append(words[punctuationLength]);
            } else { // Dialogue ends with punctuation
                result.append(Mood.combineText(words, punctuation, wordLength));
            }
        } else { // Dialogue begins with punctuation
            if (punctuationLength > wordLength) { // Dialogue ends with punctuation
                result.append(Mood.combineText(punctuation, words, wordLength));
                result.append(punctuation[wordLength]);
            } else { // Dialogue ends with word
                result.append(Mood.combineText(punctuation, words, punctuationLength));
            }
        }

        return result.toString();

    }

    /**
     * This method removes leading and trailing empty strings from a given string array.
     *
     * @param array the given string array.
     * @return the stripped array.
     */
    private static String[] strip(String[] array) {

        if (array.length == 0) { // Zero elements
            return array;
        } else if (array.length == 1) { // One element
            if (array[0].isEmpty()) {
                return new String[0];
            } else {
                return array;
            }
        }

        ArrayList<String> temp = new ArrayList<>(Arrays.asList(array));
        if (temp.get(0).isEmpty() && temp.get(temp.size() - 1).isEmpty()) { // First and last elements are empty
            temp.remove(0);
            temp.remove(temp.size() - 1);
        } else if (temp.get(0).isEmpty()) { // First element is empty
            temp.remove(0);
        } else if (temp.get(temp.size() - 1).isEmpty()) { // Last element is empty
            temp.remove(temp.size() - 1);
        }
        String[] result = new String[temp.size()];

        return temp.toArray(result);

    }

    /**
     * This method paraphrases a single word using appropriate synonyms.
     *
     * @param word the word to paraphrase.
     * @return the paraphrased word.
     */
    private static String paraphraseWord(String word, HashMap<String, String[]> synonyms) {

        if (word.isEmpty()) {
            return "";
        }

        String format = word.replaceAll("(^[^a-zA-Z]+|[^a-zA-Z]+$)", "");
        String lowercase = format.toLowerCase();

        String[] possibleSynonyms = synonyms.get(lowercase);
        if (possibleSynonyms == null) {
            return word;
        }

        int rng = (int) Math.floor(Math.random()*(possibleSynonyms.length));
        String synonym = possibleSynonyms[rng];

        if (Character.isUpperCase(format.charAt(0))) {
            synonym = Character.toString(synonym.charAt(0)).toUpperCase() + synonym.substring(1);
        }

        return word.replaceAll(format, synonym);

    }

    /**
     * This method concatenates pairs of corresponding text within two string arrays.
     *
     * @param first the first string array.
     * @param second the second string array.
     * @param length the number of pairs.
     * @return the concatenated text.
     */
    private static StringBuilder combineText(String[] first, String[] second, int length) {

        StringBuilder constructor = new StringBuilder();
        for (int i = 0; i < length; i++) {
            constructor.append(first[i]);
            constructor.append(second[i]);
        }

        return constructor;

    }

}
