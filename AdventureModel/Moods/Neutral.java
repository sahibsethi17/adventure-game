package AdventureModel.Moods;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

public class Neutral implements Mood, Serializable {

    @Serial
    private static final long serialVersionUID = -6731425071213512513L;

    /**
     * A HashMap of words to synonyms which convey a neutral tone.
     */
    protected static HashMap<String, String[]> neutralSynonyms = new HashMap<>();

    /**
     * The accumulated opinion points.
     */
    private int opinion;

    public Neutral() {
        this.opinion = PROMOTION_THRESHOLD / 2;
    }

    protected Neutral(int opinion) {
        this.opinion = opinion;
    }

    @Override
    public String paraphrase(String dialogue) {
        return Mood.paraphraseDialogue(dialogue, Neutral.neutralSynonyms);
    }

    @Override
    public boolean isGenerous() {
        return opinion >= PROMOTION_THRESHOLD / 2;
    }

    @Override
    public Mood update() {

        boolean friendly = this.opinion >= PROMOTION_THRESHOLD;
        boolean hostile = this.opinion < DEMOTION_THRESHOLD;

        if (friendly) {
            return this.promote();
        } else if (hostile) {
            return this.demote();
        } else {
            return this;
        }

    }

    public Mood promote() {
        return new Friendly(this.opinion - PROMOTION_THRESHOLD);
    }

    public Mood demote() {
        return new Hostile(this.opinion + PROMOTION_THRESHOLD);
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
