package ConnectDB;

import Entity.*;
import Menu.MainMenu;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static headers.Windows.SIZE_SMALL_WINDOW_X;
import static headers.Windows.SIZE_SMALL_WINDOW_Y;

public class ConnectionToDB extends JFrame {
    private boolean connectedToDB = false;
    private static SessionFactory sessionFactory;

    public ConnectionToDB() {
        setTitle("Подключение");
        setSize(SIZE_SMALL_WINDOW_X, SIZE_SMALL_WINDOW_Y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel statusLabel = new JLabel("Подключение к серверу");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(statusLabel, BorderLayout.CENTER);
        setVisible(true);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        JButton authButton = new JButton("Авторизоваться");
        authButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Auth auth = new Auth(sessionFactory);
                auth.setVisible(true);
                dispose();
            }
        });
        JButton regButton = new JButton("Зарегистрироваться");
        regButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Register register = new Register(sessionFactory);
                register.setVisible(true);
                dispose();
            }
        });
        JButton playButton = new JButton("Играть оффлайн");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
                dispose();
            }
        });
        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel);

        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(InfoPlayer.class)
                    .buildSessionFactory();
            statusLabel.setText("Подключено к серверу");
            buttonPanel.add(authButton);
            buttonPanel.add(regButton);
        } catch (Exception ex) {
            statusLabel.setText("<html>Ошибка подключения. Попробуйте позже или можете играть оффлайн.<br>В оффлайне прогресс не сохраняется</html>");
            panel.add(playButton, BorderLayout.SOUTH);
            ex.printStackTrace();
        }
    }
}
