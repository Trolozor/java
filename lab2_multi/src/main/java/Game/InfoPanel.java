package Game;

import Players.*;
import headers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class InfoPanel extends JPanel implements Windows {
    private Client client;
    private Map<Integer, Player> players;
    JPanel playerInfoPanel;
    private Map<Player, PlayerInfo> infoMap = new HashMap<>();
    private JLabel instruction;
    private JButton startButton ;
    private JButton pauseButton;
    private JButton exitButton;
    private JLabel timer;

    public InfoPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Windows.SIZE_PANEL_Y + 100, Windows.SIZE_WINDOW_Y));
        setBackground(Color.LIGHT_GRAY);
        playerInfoPanel = new JPanel();
        playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));

        JPanel buttonInfoPanel = new JPanel();
        buttonInfoPanel.setLayout(new GridLayout(3, 1));
        addButtonControl(buttonInfoPanel);

        JPanel instructionPanel = new JPanel();
        instruction = new JLabel("<html>Управление:<br>Движение: A/D<br>Стрельба: Space/Enter</html>");
        instructionPanel.add(instruction);

        timer = new JLabel("<html>Оставшееся время: </html>");
        instructionPanel.add(timer);

        add(playerInfoPanel, BorderLayout.NORTH);
        add(buttonInfoPanel, BorderLayout.SOUTH);
        add(instructionPanel, BorderLayout.CENTER);
    }

    public void addPlayerInfo(Player player) {
        PlayerInfo playerInfo = new PlayerInfo(player);
        infoMap.put(player, playerInfo);
        playerInfoPanel.add(playerInfo);
    }
    public void updateInfo(Player player) {
        infoMap.get(player).getHitsLabel().setText("Количество выстрелов: " + player.getHits());
        infoMap.get(player).getScoreLabel().setText("Счет игрока: " + player.getScore());
        infoMap.get(player).getWinsLabel().setText("Количество побед: " + player.getWins());

    }
    public void setClient(Client client) {
        this.client = client;
    }
    public void setPlayers(Map<Integer, Player> players){
        this.players = players;
    }
    public void isSingle() {
        if(players.size() <= 1) {
            instruction.setText("<html>Управление:<br>Движение: A/D<br>Стрельба: Space/Enter <br> <br> <br>Прогресс в одиночной игре <br> <b>не сохраняется!<b></html>");
        }
    }

    public void disableStart() {
        startButton.setEnabled(false);
    }
    public void disablePause() {
        pauseButton.setEnabled(false);
    }
    public void enableStart() {
        startButton.setEnabled(true);
    }
    public void enablePause() {
        pauseButton.setEnabled(true);
    }
    public void setTimer(int time) {
        if (time >= 60) {
            int minutes = time / 60;
            int remainingSeconds = time % 60;
            timer.setText("Время игры: " + minutes + " мин " + remainingSeconds + " сек");
        } else {
            timer.setText("Время игры: " + time + " сек");
        }
    }
    private void addButtonControl(JPanel buttonInfoPanel) {
        startButton = new JButton("Начать игру");
        pauseButton = new JButton("Пауза");
        exitButton = new JButton("Выход в меню");
        startButton.setFocusable(false);
        pauseButton.setFocusable(false);
        exitButton.setFocusable(false);

        startButton.setPreferredSize(getButtonPreferredSizeSmall());

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.startGame();
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.stopGame();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.exitGame();
            }
        });

        buttonInfoPanel.add(startButton);
        buttonInfoPanel.add(pauseButton);
        buttonInfoPanel.add(exitButton);
    }
    private class PlayerInfo extends JPanel {
        private JLabel playerNameLabel;
        private JLabel scoreLabel;
        private JLabel hitsLabel;
        private JLabel winsLabel;
        private Player player;

        public PlayerInfo(Player player) {
            this.player = player;
            playerNameLabel = new JLabel("Игрок: " + player.getName());
            scoreLabel = new JLabel("Счет игрока: " + player.getScore());
            hitsLabel = new JLabel("Количество выстрелов: " + player.getHits());
            winsLabel = new JLabel("Количество побед: " + player.getWins());

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            add(playerNameLabel);
            add(Box.createVerticalStrut(4));
            add(scoreLabel);
            add(Box.createVerticalStrut(4));
            add(hitsLabel);
            add(Box.createVerticalStrut(4));
            add(winsLabel);
            add(new JSeparator(SwingConstants.HORIZONTAL));
            add(Box.createVerticalStrut(7));
        }

        public JLabel getPlayerNameLabel() {
            return playerNameLabel;
        }

        public void setPlayerNameLabel(JLabel playerNameLabel) {
            this.playerNameLabel = playerNameLabel;
        }

        public JLabel getScoreLabel() {
            return scoreLabel;
        }

        public void setScoreLabel(JLabel scoreLabel) {
            this.scoreLabel = scoreLabel;
        }

        public JLabel getHitsLabel() {
            return hitsLabel;
        }

        public void setHitsLabel(JLabel hitsLabel) {
            this.hitsLabel = hitsLabel;
        }

        public JLabel getWinsLabel() {
            return winsLabel;
        }

        public void setWinsLabel(JLabel winsLabel) {
            this.winsLabel = winsLabel;
        }
    }
}