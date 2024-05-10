package Menu;

import Players.*;
import headers.*;
import Servers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NetworkGameMenu extends JFrame implements Windows {

    private JButton addServerButton;
    private JButton removeServerButton;
    private JButton connectButton;
    private JButton backButton;
    private MainMenu mainMenu;
    private ServerPanel serverPanel;
    private Player player;

    public NetworkGameMenu(MainMenu mainMenu, Player player) {
        super("Network Game Menu");
        this.player = player;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(Windows.SIZE_WINDOW_X, Windows.SIZE_WINDOW_Y);
        setLocationRelativeTo(null);

        this.mainMenu = mainMenu;
        serverPanel = new ServerPanel();

        // Создаем кнопки
        addServerButton = new JButton("Добавить сервер");
        removeServerButton = new JButton("Удалить сервер");
        connectButton = new JButton("Подключиться");
        backButton = new JButton("В меню");

        addServerButton.setPreferredSize(getButtonPreferredSize());
        removeServerButton.setPreferredSize(getButtonPreferredSize());
        connectButton.setPreferredSize(getButtonPreferredSize());
        backButton.setPreferredSize(getButtonPreferredSize());

        // Создаем панель для кнопок
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0)); // Одна строка, четыре столбца
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(addServerButton);
        buttonPanel.add(removeServerButton);
        buttonPanel.add(connectButton);
        buttonPanel.add(backButton);

        // Создаем панель для размещения панели с кнопками и панели сервера
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(serverPanel, BorderLayout.CENTER); // Добавляем Servers.ServerPanel в центр

        // Добавляем панель с кнопками и панель сервера в центр окна
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // Добавляем слушателей событий для кнопок
        addServerButton.addActionListener(new AddServerButtonListener());
        removeServerButton.addActionListener(new RemoveServerButtonListener());
        connectButton.addActionListener(new ConnectButtonListener());
        backButton.addActionListener(new BackButtonListener());
    }
    private class AddServerButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AddServer addServer = new AddServer(NetworkGameMenu.this, serverPanel);
        }
    }
        private class RemoveServerButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = serverPanel.getServerList().getSelectedIndex();
                if (selectedIndex != -1) {
                    // Удаляем выбранный сервер из модели списка
                    serverPanel.removeServer(selectedIndex);
                } else {
                    JOptionPane.showMessageDialog(NetworkGameMenu.this, "Выберите сервер для удаления.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private class ConnectButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client client = new Client(player);
                client.setMenu(mainMenu);
                client.setNetworkMenu(NetworkGameMenu.this);
                client.connectToServer(serverPanel.getSelectedServer());
            }
        }

        private class BackButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Добавить код для обработки нажатия кнопки "В меню"
                dispose(); // Закрываем окно сетевой игры
                mainMenu.setVisible(true); // Показываем главное меню
            }
        }
}
