package Messages;

import java.io.Serializable;

public class ExitMessage implements Serializable {
    private int ID;

    public ExitMessage(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
