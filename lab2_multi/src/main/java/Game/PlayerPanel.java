package Game;

import Players.*;
import headers.*;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class PlayerPanel extends JPanel implements Windows {
    private int xPosition = 0;
    private int moveStep = 5;
    private int sizeTriangle = 30;
    private Map<Integer, Player> players;

    public PlayerPanel() {
        setPreferredSize(new Dimension(Windows.SIZE_PANEL_X, Windows.SIZE_PANEL_Y));
        setBackground(Color.GRAY);
    }

    public void setPlayers(Map<Integer, Player> players){
        this.players = players;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(Player player : players.values()){
            player.draw(g, getWidth(), getHeight());
        }
    }
}