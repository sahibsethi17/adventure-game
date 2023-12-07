package AdventureModel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AchievementsTest {
    @Test
    void CompletionAchievementTest() {
        AdventureGame model = new AdventureGame("TinyGame");
        CompletionAchievement cA = new CompletionAchievement();
        assertEquals("completion 0\n", cA.getFileString());
        cA.updateStatus(model);
        assertEquals("completion 1\n", cA.getFileString());
    }
    @Test
    void MoodAchievementsTest() {
        MoodAchievement mA = new MoodAchievement("swing");
        assertEquals("mood swing 0\n", mA.getFileString());
        mA.setStatus(1);
        assertEquals("mood swing 1\n", mA.getFileString());
    }
    @Test
    void SecretAchivementsTest() {
        SecretAchievement sA = new SecretAchievement();
        assertEquals("secret 0\n", sA.getFileString());
        sA.setStatus(1);
        assertEquals("secret 1\n", sA.getFileString());
    }

    @Test
    void ScoreTest() {
        SecretAchievement sA = new SecretAchievement();
        MoodAchievement mA1 = new MoodAchievement("swing");
        MoodAchievement mA2 = new MoodAchievement("sadistic");
        MoodAchievement mA3 = new MoodAchievement("charismatic");
        AchievementList.getInstance().putList(sA);
        AchievementList.getInstance().putList(mA1);
        AchievementList.getInstance().putList(mA2);
        AchievementList.getInstance().putList(mA3);
        assertEquals(AchievementList.getInstance().getScore(), 0);
        AchievementList.updateScoreString();
        assertEquals( "You are a Novice!", AchievementList.getInstance().getScoreString());
        sA.setStatus(1);
        mA1.setStatus(1);
        assertEquals(AchievementList.getInstance().getScore(), 1);
        AchievementList.updateScoreString();
        assertEquals("You are an Intermediate!", AchievementList.getInstance().getScoreString());
        mA2.setStatus(1);
        assertEquals(AchievementList.getInstance().getScore(), 2);
        AchievementList.updateScoreString();
        assertEquals("You are an Expert!", AchievementList.getInstance().getScoreString());
        mA3.setStatus(1);
        assertEquals(3, AchievementList.getInstance().getScore());
        AchievementList.updateScoreString();
        assertEquals("You are a Master!", AchievementList.getInstance().getScoreString());
    }
}
