package Messages;

import java.io.Serializable;

public class GameEventMessage implements Serializable {
    public enum EventType {
        GAME_START, GAME_STOP, GAME_EXIT
    }

    private EventType eventType;
    private int playerID;

    public GameEventMessage(EventType eventType, int playerID) {
        this.eventType = eventType;
        this.playerID = playerID;
    }

    public EventType getEventType() {
        return eventType;
    }

    public int getPlayerID() {
        return playerID;
    }
}