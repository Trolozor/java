package Messages;

import Players.Player;

import java.io.Serializable;

public class ConnectMessage implements Serializable {
    private Player player;

    public ConnectMessage(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
