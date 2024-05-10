package Game;

import Players.*;
import headers.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameWindow extends JFrame implements Windows {
    private Map<Integer, Player> players = new HashMap<>();
    private InfoPanel infoPanel;
    private GamefieldPanel gamefieldPanel;
    private Client client;
    private Thread gameThread;

    public GameWindow() {
        super("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Windows.SIZE_WINDOW_X, Windows.SIZE_WINDOW_Y);
        setLocationRelativeTo(null);
    }
    public void setPlayers(Map<Integer, Player> players){
        this.players = players;
    }
    public void setClient(Client client){
        this.client = client;
    }
    public void startGame() {
        infoPanel.disableStart();
        infoPanel.enablePause();

        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();
        }

        gameThread = new Thread(this::gameLoop);
        gameThread.start();
    }
    public void pauseGame() {
        infoPanel.disablePause();
        infoPanel.enableStart();
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();
        }
    }
    public void enableButton(boolean isStop)  {
        if(isStop) {
            infoPanel.enablePause();
        } else {
            infoPanel.enableStart();
        }
    }
    public void disableButton() {
        infoPanel.disablePause();
        infoPanel.disableStart();
    }
    public void addPlayerInfo() {
        for (Map.Entry<Integer, Player> entry : players.entrySet()) {
            Player value = entry.getValue();
            infoPanel.addPlayerInfo(value);
            infoPanel.repaint();
        }
    }
    public void updateInfo(Map<Integer, Player> players) {
        this.players = players;
        System.out.println(players);
        for(Map.Entry<Integer, Player> entry : players.entrySet()) {
            Player value = entry.getValue();
            infoPanel.updateInfo(value);
        }
    }
    public GamefieldPanel getGamefieldPanel() {
        return gamefieldPanel;
    }
    public void setTime(int time) {
        infoPanel.setTimer(time);
    }
    public void shoot(Arrow arrow) {
        gamefieldPanel.addArrow(arrow);
    }
    public void movePlayer(int playerID, boolean moveLeft) {
        if(playerID <= players.size() - 1){
            players.get(playerID).move(moveLeft, gamefieldPanel.getWidth());
            gamefieldPanel.playerMoveRepaint();
        }
    }
    public void setTargets(Map<String, Integer> rounds) {
        gamefieldPanel.setTargets(rounds);
    }
    public void setPanels() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        gamefieldPanel = new GamefieldPanel(players);
        infoPanel = new InfoPanel();

        mainPanel.add(gamefieldPanel, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.EAST);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        infoPanel.setClient(client);
        infoPanel.setPlayers(players);
        infoPanel.isSingle();
        gamefieldPanel.setClient(client);
        revalidate();
        repaint();
    }
    private void gameLoop() {
        while(!Thread.currentThread().isInterrupted()) {
            gamefieldPanel.updateArrows();
            gamefieldPanel.repaint();
            gamefieldPanel.updateRounds();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
