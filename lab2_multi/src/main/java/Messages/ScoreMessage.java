package Messages;

import Players.Arrow;

import java.io.Serializable;

public class ScoreMessage implements Serializable {
    private int ID;
    private int score;

    public ScoreMessage(int ID, int score) {
        this.ID = ID;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int getID() {
        return ID;
    }
}
