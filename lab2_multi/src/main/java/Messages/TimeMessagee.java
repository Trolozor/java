package Messages;

import java.awt.*;
import java.io.Serializable;

public class TimeMessagee implements Serializable {
    private int time;
    private boolean choser;

    public TimeMessagee(int time, boolean choser) {
        this.time = time;
        this.choser = choser;
    }

    public int getTime() {
        return time;
    }

    public boolean isChoser() {
        return choser;
    }
}
