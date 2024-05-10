package Messages;

import java.io.Serializable;

public class MovePlayerMessage implements Serializable {
    private int ID;
    private boolean moveLeft;

    public MovePlayerMessage(int ID, boolean moveLeft) {
        this.ID = ID;
        this.moveLeft = moveLeft;
    }

    public boolean isMoveLeft() {
        return moveLeft;
    }

    public int getID() {
        return ID;
    }
}
