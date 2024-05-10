package Messages;

import java.io.Serializable;
import Players.Arrow;

public class ShootMessage implements Serializable {
    private int ID;
    private Arrow arrow;

    public ShootMessage(int ID, Arrow arrow) {
        this.ID = ID;
        this.arrow = arrow;
    }

    public Arrow getArrow() {
        return arrow;
    }

    public int getID() {
        return ID;
    }
}
