package Game;

import Players.*;
import headers.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GamefieldPanel extends JPanel implements Windows {
    private Client client;
    private PlayerPanel playerPanel;
    private RoundPanel roundPanel;
    private Map<Integer, Player> players;
    private List<Arrow> arrows;
    private boolean start = false;

    public GamefieldPanel(Map<Integer, Player> players) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        this.players = players;
        setFocusable(true);

        playerPanel = new PlayerPanel();
        add(playerPanel, BorderLayout.SOUTH);
        playerPanel.setPlayers(players);

        roundPanel = new RoundPanel();
        add(roundPanel, BorderLayout.NORTH);
        arrows = new ArrayList<>();
    }
    public void playerMoveRepaint() {
        playerPanel.repaint();
    }
    public void setTargets(Map<String, Integer> rounds) {
        roundPanel.setTargets(rounds);
    }
    public void updateRounds() {
        roundPanel.updateArrows();
    }
    public void addArrow(Arrow arrow) {
        arrow.setY(getHeight() - client.getPlayer().getPlayerSize());
        arrows.add(arrow);
        start = true;
    }

    public void updateArrows() {
        Iterator<Arrow> iterator = arrows.iterator();
        while (iterator.hasNext()) {
            Arrow arrow = iterator.next();
            arrow.move();
            if (arrow.getY() <= 0 || arrow.getY() <= Windows.SIZE_PANEL_Y * 2) {
                roundPanel.addArrow(arrow);
                iterator.remove();
            }
        }
    }
    public void setClient(Client client){
        this.client = client;
        roundPanel.setClient(client);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(start) {
            for (Arrow arrow : arrows) {
                arrow.draw(g);
            }
        }
    }
}
