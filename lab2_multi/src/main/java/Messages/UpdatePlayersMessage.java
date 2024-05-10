package Messages;

import Players.Player;

import java.io.Serializable;
import java.util.Map;

public class UpdatePlayersMessage implements Serializable {
    private Map<Integer, Player> players;

    public UpdatePlayersMessage(Map<Integer, Player> players) {
        this.players = players;
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }
}
