package Messages;

import Players.Player;

import java.io.Serializable;

public class SetPlayerMessage implements Serializable {
    private Player player;

    public SetPlayerMessage(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
