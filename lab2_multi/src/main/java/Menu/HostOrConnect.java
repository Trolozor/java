package Menu;

import Entity.InfoPlayer;
import Players.Player;
import Servers.CreateHost;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HostOrConnect extends JFrame {
    private SessionFactory sessionFactory;
    private boolean connectToDB = false;
    private MainMenu mainMenu;
    private int id;
    public HostOrConnect(MainMenu mainMenu) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        this.mainMenu = mainMenu;

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10)); // Одна строка, два столбца
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton connectToServerButton = new JButton("Подключиться к серверу");
        JButton becomeHostButton = new JButton("Стать хостом");

        buttonPanel.add(connectToServerButton);
        buttonPanel.add(becomeHostButton);

        getContentPane().add(buttonPanel);

        connectToServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!connectToDB) {
                    PlayerNameDialog dialog = new PlayerNameDialog(mainMenu, true, false);
                    dialog.setVisible(true);
                    dispose();
                } else {
                    Player player = new Player(getPlayerName());
                    player.setWins(getInfo());
                    dispose();
                    mainMenu.setVisible(false);

                    NetworkGameMenu networkGameMenu = new NetworkGameMenu(mainMenu, player);
                    networkGameMenu.setVisible(true);
                }
            }
        });

        becomeHostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!connectToDB) {
                    PlayerNameDialog dialog = new PlayerNameDialog(mainMenu, true, true);
                    dialog.setVisible(true);
                    dispose();
                } else {
                    Player player = new Player(getPlayerName());
                    player.setWins(getInfo());
                    dispose();
                    mainMenu.setVisible(false);

                    CreateHost createHost = new CreateHost();
                    createHost.setMainMenu(mainMenu);
                    createHost.setPlayer(player);
                    createHost.setSessionFactory(sessionFactory, id);
                    createHost.multiplayer();
                    createHost.setVisible(true);
                }
            }
        });
    }

    public void setSessionFactory(SessionFactory sessionFactory, int id) {
        this.sessionFactory = sessionFactory;
        connectToDB = true;
        this.id = id;
    }
    public String getPlayerName() {
        try (Session session = sessionFactory.openSession()) {
            InfoPlayer user = session.get(InfoPlayer.class, id);
            if (user != null) {
                return user.getUsername();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public String getInfo() {
        try (Session session = sessionFactory.openSession()) {
            InfoPlayer user = session.get(InfoPlayer.class, id);
            if (user != null) {
                return user.getInfo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

