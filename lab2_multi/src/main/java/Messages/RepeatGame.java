package Messages;

import java.io.Serializable;

public class RepeatGame implements Serializable {
    private int id;
    private boolean repeat;

    public RepeatGame(int id, boolean repeat){
        this.id = id;
        this.repeat = repeat;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public int getId() {
        return id;
    }
}
