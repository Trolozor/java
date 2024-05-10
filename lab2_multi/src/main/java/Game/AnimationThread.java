package Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnimationThread extends Thread {
    private long delayMillis;
    private Map<String, Integer> coordinateTargets = new HashMap<>();

    public AnimationThread() {

        this.delayMillis = 100;
    }

    @Override
    public void run() {

    }
}
