package AdventureModel.Moods;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

public class Hostile implements Mood, Serializable {

    @Serial
    private static final long serialVersionUID = -6731425071213512513L;

    /**
     * A HashMap of words to synonyms which convey a hostile tone.
     */
    protected static HashMap<String, String[]> hostileSynonyms = new HashMap<>();

    /**
     * Tracks whether any hostile entity ever has less than  <code>DEMOTION_THRESHOLD</code> opinion points.
     */
    public static boolean livid = false;

    /**
     * Tracks whether any mood swing ever occurs from hostile to friendly.
     */
    public static boolean friendlyMoodSwing = false;

    /**
     * The accumulated opinion points.
     */
    private int opinion;

    public Hostile() {
        this.opinion = PROMOTION_THRESHOLD / 2;
    }

    protected Hostile(int opinion) {
        this.opinion = opinion;
    }

    @Override
    public String paraphrase(String dialogue) {
        return Mood.paraphraseDialogue(dialogue, Hostile.hostileSynonyms);
    }

    @Override
    public boolean isGenerous() {
        return false;
    }

    @Override
    public Mood update() {

        boolean friendly = this.opinion >= 2*PROMOTION_THRESHOLD;
        boolean neutral = this.opinion >= PROMOTION_THRESHOLD;
        boolean livid = this.opinion < DEMOTION_THRESHOLD;

        if (friendly) {
            Hostile.friendlyMoodSwing = true;
            return this.promote().promote();
        } else if (neutral) {
            return this.promote();
        } else {
            if (livid) {
                Hostile.livid = true;
            }
            return this;
        }

    }

    public Mood promote() {
        return new Neutral(this.opinion - PROMOTION_THRESHOLD);
    }

    public Mood demote() {
        return this;
    }

    @Override
    public void increaseOpinion(int opinion) {
        this.opinion += opinion;
    }

    @Override
    public void decreaseOpinion(int opinion) {
        this.opinion -= opinion;
    }

    @Override
    public void setOpinion(int opinion) {
        this.opinion = opinion;
    }

    @Override
    public int getOpinion() {
        return this.opinion;
    }

}
