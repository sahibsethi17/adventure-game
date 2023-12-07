package AdventureModel.Moods;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;

public class MoodTest {

    @Test
    void loadMoodSynonymsTest() throws IOException {

        assertTrue(Friendly.friendlySynonyms.isEmpty());
        assertTrue(Neutral.neutralSynonyms.isEmpty());
        assertTrue(Hostile.hostileSynonyms.isEmpty());

        Mood.loadSynonyms("AdventureModel/Moods");

        HashMap<String, String[]> synonyms = new HashMap<>();
        synonyms.put("lorem", new String[] {"l1"});
        synonyms.put("ipsum", new String[] {"i1", "i2"});
        synonyms.put("dolor", new String[] {"d1"});
        synonyms.put("sit", new String[] {"s1"});

        assertEquals(synonyms.keySet(), Friendly.friendlySynonyms.keySet());
        assertEquals(synonyms.keySet(), Neutral.neutralSynonyms.keySet());
        assertEquals(synonyms.keySet(), Hostile.hostileSynonyms.keySet());

        for (int i = 0; i < synonyms.get("lorem").length; i++) {
            assertEquals(synonyms.get("lorem")[i], Friendly.friendlySynonyms.get("lorem")[i]);
            assertEquals(synonyms.get("lorem")[i], Neutral.neutralSynonyms.get("lorem")[i]);
            assertEquals(synonyms.get("lorem")[i], Hostile.hostileSynonyms.get("lorem")[i]);
        }

        for (int i = 0; i < synonyms.get("ipsum").length; i++) {
            assertEquals(synonyms.get("ipsum")[i], Friendly.friendlySynonyms.get("ipsum")[i]);
            assertEquals(synonyms.get("ipsum")[i], Neutral.neutralSynonyms.get("ipsum")[i]);
            assertEquals(synonyms.get("ipsum")[i], Hostile.hostileSynonyms.get("ipsum")[i]);
        }

        for (int i = 0; i < synonyms.get("dolor").length; i++) {
            assertEquals(synonyms.get("dolor")[i], Friendly.friendlySynonyms.get("dolor")[i]);
            assertEquals(synonyms.get("dolor")[i], Neutral.neutralSynonyms.get("dolor")[i]);
            assertEquals(synonyms.get("dolor")[i], Hostile.hostileSynonyms.get("dolor")[i]);
        }

        for (int i = 0; i < synonyms.get("sit").length; i++) {
            assertEquals(synonyms.get("sit")[i], Friendly.friendlySynonyms.get("sit")[i]);
            assertEquals(synonyms.get("sit")[i], Neutral.neutralSynonyms.get("sit")[i]);
            assertEquals(synonyms.get("sit")[i], Hostile.hostileSynonyms.get("sit")[i]);
        }

    }

    @Test
    void paraphraseTest() {

        // Synonym dictionaries
        HashMap<String, String[]> none = new HashMap<>();
        HashMap<String, String[]> synonyms = new HashMap<>();

        synonyms.put("lorem", new String[] {"l1", "l2", "l3"}); // First word
        synonyms.put("ipsum", new String[] {"i1", "i2", "i3"}); // Lowercase
        synonyms.put("amet", new String[] {"a1", "a2", "a3"}); // Comma
        synonyms.put("elit", new String[] {"e1", "e2", "e3"}); // Period
        synonyms.put("proin", new String[] {"p1", "p2", "p3"}); // Capitalized
        synonyms.put("non", new String[] {"n1", "n2", "n3"}); // Hyphen
        synonyms.put("nisl", new String[] {"n1", "n2", "n3"}); // Hyphen
        synonyms.put("vel", new String[] {"v1", "v2", "v3"}); // Duplicate
        synonyms.put("blandit", new String[] {"b1", "b2", "b3"}); // Period
        synonyms.put("quisque", new String[] {"q1", "q2", "q3"}); // Capitalized
        synonyms.put("eros", new String[] {"e1", "e2", "e3"}); // Comma
        synonyms.put("rhoncus", new String[] {"r1", "r2", "r3"}); // Complex separators
        synonyms.put("tortor", new String[] {"t1", "t2", "t3"}); // Complex separators
        synonyms.put("tempor", new String[] {"t1", "t2", "t3"}); // Last word

        // Words
        String empty = "";
        assertEquals(empty, Mood.paraphraseDialogue(empty, synonyms));

        String punctuation = "``~~!!@@##$$%%^^&&**(())--__==++[[{{]]}}\\\\||;;::''\"\",,<<..>>//??";
        assertEquals(punctuation, Mood.paraphraseDialogue(punctuation, none));
        assertEquals(punctuation, Mood.paraphraseDialogue(punctuation, synonyms));

        String capitalized = "Lorem";
        String leadingCapitalized = punctuation + capitalized;
        String trailingCapitalized = capitalized + punctuation;
        String bothCapitalized = punctuation + capitalized + punctuation;
        assertTrue(Mood.paraphraseDialogue(capitalized, synonyms).matches("(L1|L2|L3)"));
        assertTrue(Mood.paraphraseDialogue(leadingCapitalized, synonyms).matches("[^a-zA-Z]+(L1|L2|L3)"));
        assertTrue(Mood.paraphraseDialogue(trailingCapitalized, synonyms).matches("(L1|L2|L3)[^a-zA-Z]+"));
        assertTrue(Mood.paraphraseDialogue(bothCapitalized, synonyms).matches("[^a-zA-Z]+(L1|L2|L3)[^a-zA-Z]+"));

        String lowercase = capitalized.toLowerCase();
        String leadingLowercase = punctuation + lowercase;
        String trailingLowercase = lowercase + punctuation;
        String bothLowercase = punctuation + lowercase + punctuation;
        assertTrue(Mood.paraphraseDialogue(lowercase, synonyms).matches("(l1|l2|l3)"));
        assertTrue(Mood.paraphraseDialogue(leadingLowercase, synonyms).matches("[^a-zA-Z]+(l1|l2|l3)"));
        assertTrue(Mood.paraphraseDialogue(trailingLowercase, synonyms).matches("(l1|l2|l3)[^a-zA-Z]+"));
        assertTrue(Mood.paraphraseDialogue(bothLowercase, synonyms).matches("[^a-zA-Z]+(l1|l2|l3)[^a-zA-Z]+"));

        String uppercase = capitalized.toUpperCase();
        String leadingUppercase = punctuation + uppercase;
        String trailingUppercase = uppercase + punctuation;
        String bothUppercase = punctuation + uppercase + punctuation;
        assertTrue(Mood.paraphraseDialogue(uppercase, synonyms).matches("(L1|L2|L3)"));
        assertTrue(Mood.paraphraseDialogue(leadingUppercase, synonyms).matches("[^a-zA-Z]+(L1|L2|L3)"));
        assertTrue(Mood.paraphraseDialogue(trailingUppercase, synonyms).matches("(L1|L2|L3)[^a-zA-Z]+"));
        assertTrue(Mood.paraphraseDialogue(bothUppercase, synonyms).matches("[^a-zA-Z]+(L1|L2|L3)[^a-zA-Z]+"));

        // Sentence
        String sentence = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                 Proin non-nisl vel urna maximus blandit.
                Quisque tempus condimentum eros, vel<!@!>%rhoncus-_-_-tortor""";
        String leadingSentence = punctuation + sentence;
        String trailingSentence = sentence + punctuation;
        String bothSentence = punctuation + sentence + punctuation;
        assertTrue(Mood.paraphraseDialogue(sentence, synonyms).matches("""
                (L1|L2|L3) (i1|i2|i3) dolor sit (a1|a2|a3), consectetur adipiscing (e1|e2|e3).
                 (P1|P2|P3) (n1|n2|n3)-(n1|n2|n3) (v1|v2|v3) urna maximus (b1|b2|b3).
                (Q1|Q2|Q3) tempus condimentum (e1|e2|e3), (v1|v2|v3)<!@!>%(r1|r2|r3)-_-_-(t1|t2|t3)"""));
        assertTrue(Mood.paraphraseDialogue(leadingSentence, synonyms).matches("""
                [^a-zA-Z]+(L1|L2|L3) (i1|i2|i3) dolor sit (a1|a2|a3), consectetur adipiscing (e1|e2|e3).
                 (P1|P2|P3) (n1|n2|n3)-(n1|n2|n3) (v1|v2|v3) urna maximus (b1|b2|b3).
                (Q1|Q2|Q3) tempus condimentum (e1|e2|e3), (v1|v2|v3)<!@!>%(r1|r2|r3)-_-_-(t1|t2|t3)"""));
        assertTrue(Mood.paraphraseDialogue(trailingSentence, synonyms).matches("""
                (L1|L2|L3) (i1|i2|i3) dolor sit (a1|a2|a3), consectetur adipiscing (e1|e2|e3).
                 (P1|P2|P3) (n1|n2|n3)-(n1|n2|n3) (v1|v2|v3) urna maximus (b1|b2|b3).
                (Q1|Q2|Q3) tempus condimentum (e1|e2|e3), (v1|v2|v3)<!@!>%(r1|r2|r3)-_-_-(t1|t2|t3)[^a-zA-Z]+"""));
        assertTrue(Mood.paraphraseDialogue(bothSentence, synonyms).matches("""
                [^a-zA-Z]+(L1|L2|L3) (i1|i2|i3) dolor sit (a1|a2|a3), consectetur adipiscing (e1|e2|e3).
                 (P1|P2|P3) (n1|n2|n3)-(n1|n2|n3) (v1|v2|v3) urna maximus (b1|b2|b3).
                (Q1|Q2|Q3) tempus condimentum (e1|e2|e3), (v1|v2|v3)<!@!>%(r1|r2|r3)-_-_-(t1|t2|t3)[^a-zA-Z]+"""));

    }

    @Test
    void pacifyTest() {

        Mood mood = new Hostile();

        mood.increaseOpinion(50);
        mood = mood.update();
        assertTrue(mood instanceof Neutral);

        mood.increaseOpinion(100);
        mood = mood.update();
        assertTrue(mood instanceof Friendly);

        mood.increaseOpinion(100);
        mood = mood.update();
        assertTrue(mood instanceof Friendly);

    }

    @Test
    void provokeTest() {

        Mood mood = new Friendly();

        mood.decreaseOpinion(51);
        mood = mood.update();
        assertTrue(mood instanceof Neutral);

        mood.decreaseOpinion(100);
        mood = mood.update();
        assertTrue(mood instanceof Hostile);

        mood.decreaseOpinion(100);
        mood = mood.update();
        assertTrue(mood instanceof Hostile);

    }

    @Test
    void moodSwingTest() {

        Mood friendly = new Friendly();
        Mood hostile = new Hostile();

        friendly.decreaseOpinion(151);
        friendly = friendly.update();
        assertTrue(friendly instanceof Hostile);

        hostile.increaseOpinion(150);
        hostile = hostile.update();
        assertTrue(hostile instanceof Friendly);
    }

    @Test
    void generosityTest() {

        Mood friendly = new Friendly();
        Mood neutral = new Neutral();
        Mood hostile = new Hostile();

        assertTrue(friendly.isGenerous());
        friendly.decreaseOpinion(50);
        assertTrue(friendly.isGenerous());

        assertTrue(neutral.isGenerous());
        neutral.decreaseOpinion(50);
        assertFalse(neutral.isGenerous());
        neutral.increaseOpinion(99);
        assertTrue(neutral.isGenerous());

        assertFalse(hostile.isGenerous());
        hostile.increaseOpinion(49);
        assertFalse(hostile.isGenerous());

    }

    @Test
    void charismaticAchievementTest() {

        Mood.lockCharismaticAchievement();
        Mood mood = new Friendly();

        // Achievement locked
        assertFalse(Mood.isCharismaticAchievementUnlocked());
        mood.setOpinion(Mood.PROMOTION_THRESHOLD - 1);
        assertFalse(Mood.isCharismaticAchievementUnlocked());
        mood.update();
        assertFalse(Mood.isCharismaticAchievementUnlocked());

        // Achievement unlocked
        mood.increaseOpinion(1);
        assertTrue(mood.getOpinion() >= Mood.PROMOTION_THRESHOLD);
        assertFalse(Mood.isCharismaticAchievementUnlocked());
        mood.update();
        assertTrue(mood.getOpinion() >= Mood.PROMOTION_THRESHOLD);
        assertTrue(Mood.isCharismaticAchievementUnlocked());

        // Achievement remains unlocked
        mood.decreaseOpinion(1);
        assertFalse(mood.getOpinion() >= Mood.PROMOTION_THRESHOLD);
        assertTrue(Mood.isCharismaticAchievementUnlocked());

        // Lock achievement with code
        Mood.lockCharismaticAchievement();
        assertFalse(Mood.isCharismaticAchievementUnlocked());

        // Unlock achievement with code
        Mood.unlockCharismaticAchievement();
        assertTrue(Mood.isCharismaticAchievementUnlocked());

    }

    @Test
    void sadisticAchievementTest() {

        Mood.lockSadisticAchievement();
        Mood mood = new Hostile();

        // Achievement locked
        assertFalse(Mood.isSadisticAchievementUnlocked());
        mood.setOpinion(Mood.DEMOTION_THRESHOLD);
        assertFalse(Mood.isSadisticAchievementUnlocked());
        mood.update();
        assertFalse(Mood.isSadisticAchievementUnlocked());

        // Achievement unlocked
        mood.decreaseOpinion(1);
        assertTrue(mood.getOpinion() < Mood.DEMOTION_THRESHOLD);
        assertFalse(Mood.isSadisticAchievementUnlocked());
        mood.update();
        assertTrue(mood.getOpinion() < Mood.DEMOTION_THRESHOLD);
        assertTrue(Mood.isSadisticAchievementUnlocked());

        // Achievement remains unlocked
        mood.increaseOpinion(1);
        assertFalse(mood.getOpinion() < Mood.DEMOTION_THRESHOLD);
        assertTrue(Mood.isSadisticAchievementUnlocked());

        // Lock achievement with code
        Mood.lockSadisticAchievement();
        assertFalse(Mood.isSadisticAchievementUnlocked());

        // Unlock achievement with code
        Mood.unlockSadisticAchievement();
        assertTrue(Mood.isSadisticAchievementUnlocked());

    }

    @Test
    void moodSwingAchievementTest() {

        Mood.lockMoodSwingAchievement();
        Mood friend = new Friendly();
        Mood enemy = new Hostile();

        // Achievement locked
        assertFalse(Mood.isMoodSwingAchievementUnlocked());
        friend.decreaseOpinion(Mood.PROMOTION_THRESHOLD / 2);
        friend.decreaseOpinion(Mood.PROMOTION_THRESHOLD);
        enemy.increaseOpinion(Mood.PROMOTION_THRESHOLD / 2);
        enemy.increaseOpinion(Mood.PROMOTION_THRESHOLD - 1);
        assertFalse(Friendly.hostileMoodSwing);
        assertFalse(Hostile.friendlyMoodSwing);
        assertFalse(Mood.isMoodSwingAchievementUnlocked());

        // Achievement unlocked

        // Friendly mood swing
        friend.decreaseOpinion(1);
        friend.update();
        assertTrue(Friendly.hostileMoodSwing);
        assertTrue(Mood.isMoodSwingAchievementUnlocked());
        friend.increaseOpinion(1);
        Friendly.hostileMoodSwing = false;

        // Hostile mood swing
        enemy.increaseOpinion(1);
        enemy.update();
        assertTrue(Hostile.friendlyMoodSwing);
        assertTrue(Mood.isMoodSwingAchievementUnlocked());
        enemy.decreaseOpinion(1);
        Hostile.friendlyMoodSwing = false;

        // Both mood swings
        friend.decreaseOpinion(1);
        enemy.increaseOpinion(1);
        friend.update();
        enemy.update();
        assertTrue(Friendly.hostileMoodSwing);
        assertTrue(Hostile.friendlyMoodSwing);
        assertTrue(Mood.isMoodSwingAchievementUnlocked());

        // Achievement remains unlocked
        friend.increaseOpinion(1);
        enemy.decreaseOpinion(1);
        friend.update();
        enemy.update();
        assertTrue(Mood.isMoodSwingAchievementUnlocked());

        // Lock achievement with code
        Mood.lockMoodSwingAchievement();
        assertFalse(Mood.isMoodSwingAchievementUnlocked());

        // Unlock achievement with code
        Mood.unlockMoodSwingAchievement();
        assertTrue(Mood.isMoodSwingAchievementUnlocked());

    }

}
