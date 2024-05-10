package Messages;

import java.io.Serializable;

public class EndGameMessage implements Serializable {
    private int ID, score;
    private boolean isEquals;

    public EndGameMessage(int ID, int score, boolean isEquals) {
        this.ID = ID;
        this.score = score;
        this.isEquals = isEquals;
    }

    public int getID() {
        return ID;
    }

    public int getScore() {
        return score;
    }

    public boolean isEquals() {
        return isEquals;
    }
}
