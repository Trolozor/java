package Messages;

import Players.Player;

import java.awt.*;
import java.io.Serializable;

public class SetColorMessage implements Serializable {
    private Player player;
    private Color color;

    public SetColorMessage(Player player, Color color) {
        this.player = player;
        this.color = color;
    }

    public Player getPlayer() {
        return player;
    }

    public Color getColor() {
        return color;
    }
}
