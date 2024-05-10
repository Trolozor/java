package Menu;

import Entity.InfoPlayer;
import Players.Player;
import Servers.CreateHost;
import headers.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame implements Windows {

    private JButton singlePlayerButton;
    private JButton networkGameButton;
    private JButton settingsButton;
    private JButton exitButton;
    private SessionFactory sessionFactory;
    private boolean connectToDB = false;
    private int id;


    public MainMenu() {
        super("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Windows.SIZE_WINDOW_X, Windows.SIZE_WINDOW_Y);
        setLocationRelativeTo(null);

        // Создаем кнопки
        singlePlayerButton = new JButton("Одиночный режим");
        networkGameButton = new JButton("Сетевая игра");
        settingsButton = new JButton("Настройки");
        exitButton = new JButton("Выход");

        singlePlayerButton.setPreferredSize(getButtonPreferredSize());
        networkGameButton.setPreferredSize(getButtonPreferredSize());
        settingsButton.setPreferredSize(getButtonPreferredSize());
        exitButton.setPreferredSize(getButtonPreferredSize());

        // Создаем панель для кнопок
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(singlePlayerButton);
        buttonPanel.add(networkGameButton);
        buttonPanel.add(settingsButton);
        buttonPanel.add(exitButton);

        // Добавляем панель с кнопками по центру окна
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        getContentPane().add(buttonPanel, gbc);

        // Добавляем слушателей событий для кнопок
        singlePlayerButton.addActionListener(new SinglePlayerButtonListener());
        networkGameButton.addActionListener(new NetworkGameButtonListener());
        settingsButton.addActionListener(new SettingsButtonListener());
        exitButton.addActionListener(new ExitButtonListener());
    }
    public void setSessionFactory(SessionFactory sessionFactory, int id) {
        this.sessionFactory = sessionFactory;
        connectToDB = true;
        this.id = id;
    }
    private class SinglePlayerButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!connectToDB) {
                PlayerNameDialog dialog = new PlayerNameDialog(MainMenu.this, false, false);
                dialog.setVisible(true);
            } else {
                Player player = new Player(getPlayerName());
                player.setWins(getInfo());
                MainMenu.this.setVisible(false);

                CreateHost createHost = new CreateHost();
                createHost.setPlayer(player);
                createHost.setMainMenu(MainMenu.this);
                createHost.setSessionFactory(sessionFactory, id);
                createHost.singleplayer();
                createHost.setVisible(true);
            }
        }
    }

    private class NetworkGameButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            HostOrConnect hostOrConnect = new HostOrConnect(MainMenu.this);
            if(connectToDB) {
                hostOrConnect.setSessionFactory(sessionFactory, id);
            }
            hostOrConnect.setVisible(true);
        }
    }

    private class SettingsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(MainMenu.this, "Настройки");
        }
    }

    private class ExitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
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
