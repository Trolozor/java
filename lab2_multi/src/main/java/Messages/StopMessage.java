package Messages;

import java.io.Serializable;

public class StopMessage implements Serializable {
    private boolean stop;
    private int playerID;

    public StopMessage(boolean stop, int playerID) {
        this.stop = stop;
        this.playerID = playerID;
    }

    public boolean getStop() {
        return stop;
    }

    public int getPlayerID() {
        return playerID;
    }
}
