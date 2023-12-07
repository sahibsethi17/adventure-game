package AdventureModel.Moods;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

public class Friendly implements Mood, Serializable {

    @Serial
    private static final long serialVersionUID = -6731425071213512513L;

    /**
     * A HashMap of words to synonyms which convey a friendly tone.
     */
    protected static HashMap<String, String[]> friendlySynonyms = new HashMap<>();

    /**
     * Tracks whether any friendly entity ever reaches or exceeds <code>PROMOTION_THRESHOLD</code> opinion points.
     */
    public static boolean ecstatic = false;

    /**
     * Tracks whether any mood swing ever occurs from friendly to hostile.
     */
    public static boolean hostileMoodSwing = false;

    /**
     * The accumulated opinion points.
     */
    private int opinion;

    public Friendly() {
        this.opinion = PROMOTION_THRESHOLD / 2;
    }

    protected Friendly(int opinion) {
        this.opinion = opinion;
    }

    @Override
    public String paraphrase(String dialogue) {
        return Mood.paraphraseDialogue(dialogue, Friendly.friendlySynonyms);
    }

    @Override
    public boolean isGenerous() {
        return true;
    }

    @Override
    public Mood update() {

        boolean neutral = this.opinion < DEMOTION_THRESHOLD;
        boolean hostile = this.opinion < DEMOTION_THRESHOLD - PROMOTION_THRESHOLD;
        boolean ecstatic = this.opinion >= 100;

        if (hostile) {
            Friendly.hostileMoodSwing = true;
            return this.demote().demote();
        } else if (neutral) {
            return this.demote();
        } else {
            if (ecstatic) {
                Friendly.ecstatic = true;
            }
            return this;
        }

    }

    public Mood promote() {
        return this;
    }

    public Mood demote() {
        return new Neutral(this.opinion + PROMOTION_THRESHOLD);
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
