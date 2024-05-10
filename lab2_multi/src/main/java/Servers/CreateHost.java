package Servers;

import Players.*;
import headers.*;
import Menu.MainMenu;
import org.hibernate.SessionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;


public class CreateHost extends JFrame implements Windows, Serializable {

    private DefaultTableModel tableModel;
    private Player player;
    private MainMenu mainMenu;
    private Client client;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JScrollPane scrollPane;
    private JPanel inputPanel;
    private JSlider timeSlider;
    private SessionFactory sessionFactory;
    private int id;
    private boolean connectToDB = false;

    public CreateHost() {
        setTitle("Создание хоста");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(Windows.SIZE_WINDOW_X, Windows.SIZE_WINDOW_Y);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                    dispose();
                    System.exit(0);
            }
        });

        leftPanel = new JPanel();
        rightPanel = new JPanel();

        leftPanel.setLayout(new BorderLayout());
        rightPanel.setLayout(new BorderLayout());

        leftPanel.setBackground(Color.LIGHT_GRAY);
        rightPanel.setBackground(Color.WHITE);

        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 1, 0, 10));

        leftPanel.add(inputPanel, BorderLayout.NORTH);

        leftPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);

        Dimension leftPanelSize = new Dimension(getWidth() / 3, getHeight());

        leftPanel.setPreferredSize(leftPanelSize);
        leftPanel.setMinimumSize(leftPanelSize);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(rightPanel, BorderLayout.CENTER);
        getContentPane().add(leftPanel, BorderLayout.EAST);
    }

    public void multiplayer() {
        JLabel playersLabel = new JLabel("Максимальное количество игроков: 1");
        JSlider playersSlider = new JSlider(1, 4, 1);
        playersSlider.setMajorTickSpacing(1);
        playersSlider.setPaintTicks(true);
        playersSlider.setPaintLabels(true);
        playersSlider.addChangeListener(e -> playersLabel.setText("Максимальное количество игроков: " + playersSlider.getValue()));
        inputPanel.add(playersLabel);
        inputPanel.add(playersSlider);
        setTimerSlider();
        createServer(playersSlider.getValue(), "Создать сервер", true);
    }
    public void singleplayer() {
        setTimerSlider();
        createServer(0, "Задать время", false);
    }
    public void setTimerSlider() {
        JLabel timeLabel = new JLabel("Время игры: 10 сек");
        timeSlider = new JSlider(10, 180, 10);
        timeSlider.setMajorTickSpacing(10);
        timeSlider.setPaintTicks(true);
        timeSlider.setPaintLabels(true);
        timeSlider.addChangeListener(e -> {
            int seconds = timeSlider.getValue();
            if (seconds >= 60) {
                int minutes = seconds / 60;
                int remainingSeconds = seconds % 60;
                timeLabel.setText("Время игры: " + minutes + " мин " + remainingSeconds + " сек");
            } else {
                timeLabel.setText("Время игры: " + seconds + " сек");
            }
        });
        inputPanel.add(timeLabel);
        inputPanel.add(timeSlider);
    }
    public void createServer(int countOfPlayers, String nameButton, boolean isMulti) {
        JButton buttonCreate = new JButton(nameButton);
        buttonCreate.addActionListener(e -> {
            int port = 1234;
            Server server = new Server(player.getName(), port, countOfPlayers, timeSlider.getValue());
            if(connectToDB) {
                server.setSessionFactory(sessionFactory);
            }
            try {
                server.startServer();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            String serverAddress = server.getServerIPAddress().getHostAddress() + ":" + port;
            if(isMulti) {
                JOptionPane.showMessageDialog(CreateHost.this, "Сервер создан " + serverAddress);
            }

            client = new Client(player);
            client.setMenu(mainMenu);
            client.connectToServer(serverAddress);

            dispose();
        });

        inputPanel.add(buttonCreate);
    }
    public void addToTable(String playerName, Color playerColor, String status) {
        Object[] rowData = {getPlayerNameWithColor(playerName, playerColor), status};
        tableModel.addRow(rowData);
    }

    private String getPlayerNameWithColor(String playerName, Color playerColor) {
        String colorHex = String.format("#%02x%02x%02x", playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue());
        String playerNameWithColor = "<html><font color='" + colorHex + "'>" + playerName + "</font></html>";
        return playerNameWithColor;
    }
    public void clientConnected() {
        inputPanel.removeAll();
        inputPanel.revalidate();
        inputPanel.repaint();

        addColorButton();
    }

    public void createButtonGame(Client client) {
        this.client = client;

        inputPanel.removeAll();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.revalidate();
        inputPanel.repaint();

        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton start = new JButton("Играть");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("CreateGame");
                client.createGame();
            }
        });

        startPanel.add(start);
        inputPanel.add(startPanel, BorderLayout.NORTH);

        addColorButton();
    }
    public void addColorButton() {
        Color[] сolors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.PINK, Color.BLACK};
        JPanel colorPanel = new JPanel(new GridLayout(2, 4));

        for (Color color : сolors) {
            JButton button = new JButton();
            button.setBackground(color);
            button.setPreferredSize(new Dimension(50, 50));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    client.setColor(color);
                }
            });
            colorPanel.add(button);
        }
        inputPanel.add(colorPanel, BorderLayout.CENTER);
        inputPanel.revalidate();
        inputPanel.repaint();
    }

    public void setMainMenu(MainMenu mainMenu){
        this.mainMenu = mainMenu;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public void clearTable() {
        tableModel.setRowCount(0);
    }
    public void createTable() {
        String[] columnNames = {"Игрок", "Статус"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        Font font = new Font("Arial", Font.PLAIN, 18);
        table.setFont(font);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        scrollPane = new JScrollPane(table);

        rightPanel.add(scrollPane);
        rightPanel.repaint();
        rightPanel.revalidate();
    }
    public void setSessionFactory(SessionFactory sessionFactory, int id) {
        this.sessionFactory = sessionFactory;
        connectToDB = true;
        this.id = id;
    }
    public void setClient(Client client) {
        this.client = client;
    }
}
