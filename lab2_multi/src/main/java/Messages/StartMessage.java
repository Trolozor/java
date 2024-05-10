package Messages;

import java.io.Serializable;

public class StartMessage implements Serializable {

    private boolean start;
    private int playerID;

    public StartMessage(boolean start, int playerID) {
        this.start = start;
        this.playerID = playerID;
    }

    public boolean getStart() {
        return start;
    }

    public int getPlayerID() {
        return playerID;
    }
}