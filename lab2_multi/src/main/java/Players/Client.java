package Players;

import Game.GameWindow;
import Game.StopOrContinueWindow;
import Game.WinWindow;
import Menu.MainMenu;
import Menu.NetworkGameMenu;
import Messages.*;
import Servers.CreateHost;
import jdk.jfr.internal.tool.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Player player;
    private Map<Integer, Player> playersMap;
    private GameWindow gameWindow;
    private CreateHost clientHost;
    private NetworkGameMenu networkGameMenu;
    private WinWindow winWindow;
    private StopOrContinueWindow stopOrContinueWindow;
    private MainMenu mainMenu;

    public Client(Player player) {
        this.player = player;
    }

    public void connectToServer(String addressInfo) {
        if (!addressInfo.isEmpty()) {
            String serverAddress = extractServerAddress(addressInfo);
            String serverPort1 = extractServerPort(addressInfo);
            int serverPort = Integer.valueOf(serverPort1);

            new Thread(() -> {
                try {
                    socket = new Socket(serverAddress, serverPort);
                    out = new ObjectOutputStream(socket.getOutputStream());
                    in = new ObjectInputStream(socket.getInputStream());

                    Object message = new ConnectMessage(player);
                    sendMessage(message);

                    while (true) {
                        try {
                            message = in.readObject();
                            handleMessage(message);
                        } catch (StreamCorruptedException e){
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }

                } catch (IOException ex) {

                }
            }).start();
        } else {
        }
    }

    public void setNetworkMenu(NetworkGameMenu networkGameMenu){
        this.networkGameMenu = networkGameMenu;
    }

    public void sendMessage(Object message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleMessage(Object message) {
        if (message instanceof String) {
            String request = (String) message;
            if(request.equals("CreateHub")) {
                clientHost = new CreateHost();
                if(networkGameMenu != null) {
                    networkGameMenu.dispose();
                }
                clientHost.setClient(this);
                clientHost.setVisible(true);
                clientHost.createTable();
                if(player.getID() == 0) {
                    clientHost.createButtonGame(this);
                } else {
                    clientHost.clientConnected();
                }
            }
            if(request.equals("CreateGame")) {
                clientHost.dispose();

                gameWindow = new GameWindow();
                gameWindow.setClient(this);
                gameWindow.setPlayers(playersMap);
                gameWindow.setPanels();
                gameWindow.addPlayerInfo();
                addKeyListenerToFrame();
                gameWindow.getGamefieldPanel().setFocusable(false);
                gameWindow.pauseGame();
                gameWindow.setVisible(true);
            }
            if(request.equals("UpdateHub")) {
                clientHost.clearTable();
                for (Map.Entry<Integer, Player> entry : playersMap.entrySet()) {
                    Player value = entry.getValue();
                    if(value.getID() == 0) {
                        clientHost.addToTable(value.getName(), value.getColor() ,  "Host");
                    } else {
                        clientHost.addToTable(value.getName(), value.getColor() , "Client");
                    }
                }
            }
            if(request.equals("ChangeName")) {
                JOptionPane.showMessageDialog(null, "Такой игрок уже есть на сервере или ваш" +
                                                                           " ник совпадает с ником другого человека", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
            if(request.equals("MaxPlayers")) {
                JOptionPane.showMessageDialog(null, "Достигнуто максимальное количество игроков","Ошибка", JOptionPane.ERROR_MESSAGE);
            }
            if(request.equals("CloseSmall")) {
                stopOrContinueWindow.dispose();
            }
            if(request.equals("StartGame")) {
                gameWindow.startGame();
                gameWindow.getGamefieldPanel().setFocusable(true);
                gameWindow.getGamefieldPanel().requestFocusInWindow();
            }
            if(request.equals("StopGame")) {
                gameWindow.pauseGame();
                gameWindow.getGamefieldPanel().setFocusable(false);
            }
            if(request.equals("HostLeave")) {
                JOptionPane.showMessageDialog(gameWindow, "Хост вышел."
                        ,"Свалил", JOptionPane.INFORMATION_MESSAGE);
                if(winWindow != null) {
                    winWindow.dispose();
                }
                if(stopOrContinueWindow != null) {
                    stopOrContinueWindow.dispose();
                }
                mainMenu.setVisible(true);
                gameWindow.dispose();
            }
        }
        if (message instanceof SetPlayerMessage) {
            player = null;
            SetPlayerMessage setPlayer = (SetPlayerMessage) message;

            player = setPlayer.getPlayer();
            System.out.println(player);
        }
        if(message instanceof UpdatePlayersMessage) {
            UpdatePlayersMessage updateMap = (UpdatePlayersMessage) message;
            playersMap = updateMap.getPlayers();
            for (Map.Entry<Integer, Player> entry : playersMap.entrySet()) {
                Player value = entry.getValue();
                System.out.println("player" + value + " name " + value.getName() + " score " + value.getScore());
            }
        }
        if(message instanceof MovePlayerMessage) {
            MovePlayerMessage movePlayer = (MovePlayerMessage) message;
            gameWindow.movePlayer(movePlayer.getID(), movePlayer.isMoveLeft());
        }
        if(message instanceof GameEventMessage) {
            GameEventMessage gameEvent = (GameEventMessage) message;
            boolean isStop = false;
            if(gameEvent.getEventType() == GameEventMessage.EventType.GAME_START) {
                isStop = false;
            }
            if(gameEvent.getEventType() == GameEventMessage.EventType.GAME_STOP) {
                isStop = true;
            }
            if(gameEvent.getEventType() == GameEventMessage.EventType.GAME_EXIT) {
                if(gameEvent.getPlayerID() == player.getID()) {
                    message = new ExitMessage(player.getID());
                    sendMessage(message);
                    mainMenu.setVisible(true);
                    gameWindow.dispose();
                    return;
                } else {
                    JOptionPane.showMessageDialog(gameWindow, "Игрок " + playersMap.get(gameEvent.getPlayerID()).getName() + " вышел."
                            ,"Свалил", JOptionPane.INFORMATION_MESSAGE);
                    playersMap.remove(gameEvent.getPlayerID());
                    gameWindow.updateInfo(playersMap);
                    return;
                }
            }
            stopOrContinueWindow = new StopOrContinueWindow(playersMap.get(gameEvent.getPlayerID()).getName(), isStop);
            stopOrContinueWindow.setClient(this);
            if(gameEvent.getPlayerID() == player.getID()) {
                stopOrContinueWindow.disableButton();
                stopOrContinueWindow.addInfoToText(true);
            }
            stopOrContinueWindow.setVisible(true);
            gameWindow.disableButton();
        }
        if(message instanceof StartMessage) {
            StartMessage start = (StartMessage) message;
            if(start.getStart()) {
                stopOrContinueWindow.playerYes(playersMap.get(start.getPlayerID()).getName());
            } else {
                stopOrContinueWindow.playerNo(playersMap.get(start.getPlayerID()).getName());
                gameWindow.enableButton(false);
            }
        }
        if(message instanceof StopMessage) {
            StopMessage stop = (StopMessage) message;
            if(stop.getStop()) {
                stopOrContinueWindow.playerYes(playersMap.get(stop.getPlayerID()).getName());
            } else {
                stopOrContinueWindow.playerNo(playersMap.get(stop.getPlayerID()).getName());
                gameWindow.enableButton(true);
            }
        }
        if(message instanceof ExitMessage) {

        }
        if(message instanceof  RepeatGame) {
            RepeatGame repeat = (RepeatGame) message;
            if(repeat.isRepeat()) {
                winWindow.playerRepeat(playersMap.get(repeat.getId()).getName());
            } else {
                winWindow.playerExit(playersMap.get(repeat.getId()).getName());
                exitGame();
            }
        }
        if(message instanceof Map) {
            Map<String, Integer> rounds = (Map<String, Integer>) message;
            gameWindow.setTargets(rounds);
        }
        if(message instanceof ShootMessage) {
            ShootMessage shoot = (ShootMessage) message;
            Arrow arrow = shoot.getArrow();
            arrow.setID(shoot.getID());
            gameWindow.shoot(arrow);
            playersMap.get(shoot.getID()).increaseHits();
            gameWindow.updateInfo(playersMap);
        }
        if(message instanceof ScoreMessage) {
            ScoreMessage score = (ScoreMessage) message;
            playersMap.get(score.getID()).increaseScore(score.getScore());
            gameWindow.updateInfo(playersMap);
        }
        if(message instanceof TimeMessagee) {
            TimeMessagee time = (TimeMessagee) message;
            if(time.isChoser()) {
                stopOrContinueWindow.updateTimer(time.getTime());
            } else {
                gameWindow.setTime(time.getTime());
            }
        }
        if(message instanceof EndGameMessage) {
            EndGameMessage endGame = (EndGameMessage) message;
            winWindow = new WinWindow(playersMap.get(endGame.getID()).getName(), endGame.getScore(), endGame.isEquals());
            winWindow.setClient(this);
            winWindow.setVisible(true);
        }
    }


    private String extractServerAddress(String serverInfo) {
        String[] parts = serverInfo.split(":");
        if (parts.length > 0) {
            return parts[0];
        } else {
            return "";
        }
    }
    private String extractServerPort(String serverInfo) {
        int separatorIndex = serverInfo.lastIndexOf(":");
        if (separatorIndex != -1) {
            return serverInfo.substring(separatorIndex + 1);
        } else {
            return "";
        }
    }
    public void createGame(){
        sendMessage("CreateGame");
    }
    public void startGame() {
        Object message = new GameEventMessage(GameEventMessage.EventType.GAME_START, player.getID());
        sendMessage(message);
    }
    public void stopGame() {
        Object message = new GameEventMessage(GameEventMessage.EventType.GAME_STOP, player.getID());
        sendMessage(message);
    }
    public void exitGame() {
        Object message = new GameEventMessage(GameEventMessage.EventType.GAME_EXIT, player.getID());
        sendMessage(message);
        gameWindow.dispose();
    }
    public void chose(boolean yes, boolean isStop) {
        if(isStop) {
            System.out.println("stop");
            if(yes) {
                Object message = new StopMessage(yes, player.getID());
                sendMessage(message);
            } else {
                Object message = new StopMessage(yes, player.getID());
                sendMessage(message);
            }
        } else {
            if(yes) {
                Object message = new StartMessage(yes, player.getID());
                sendMessage(message);
            } else {
                Object message = new StartMessage(yes, player.getID());
                sendMessage(message);
            }
        }
    }
    public void repeatGame(boolean repeat) {
        Object message = new RepeatGame(player.getID(), repeat);
        sendMessage(message);
    }
    public void setTargets(ArrayList<Rounds> rounds) {
        if(player.getID() == 0) {
            sendMessage(rounds);
        }
    }
    public void updatePlayer(int score, int id) {
        if(player.getID() == id){
            Object message = new EndGameMessage(id, score, false);
            sendMessage(message);
            message = new ScoreMessage(id, score);
            sendMessage(message);
            return;
        }
    }

    public void addKeyListenerToFrame() {
        gameWindow.getGamefieldPanel().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
                    Object message = new MovePlayerMessage(player.getID(), true);
                    sendMessage(message);

                } else if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
                    Object message = new MovePlayerMessage(player.getID(), false);
                    sendMessage(message);
                } else if(keyCode == KeyEvent.VK_SPACE) {
                    Arrow arrow = player.shoot();
                    if(arrow != null) {
                        Object message = new ShootMessage(player.getID(), arrow);
                        sendMessage(message);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }
    public Player getPlayer() {
        return player;
    }
    public void setColor(Color color) {
        Object message = new SetColorMessage(player, color);
        sendMessage(message);
    }
    public void setMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

}
