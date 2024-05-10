package Messages;

import javafx.scene.shape.Circle;

import java.io.Serializable;
import java.lang.annotation.Target;
import java.util.Map;

public class CircleMoveMessage implements Serializable {
    private Map<String, Integer> coordinateTarget;

    public CircleMoveMessage(Map<String, Integer> coordinateTarget) {
        this.coordinateTarget = coordinateTarget;
    }

    public Map<String, Integer> getCoordinateTarget() {
        return coordinateTarget;
    }
}
