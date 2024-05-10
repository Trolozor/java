package ConnectDB;

import Entity.InfoPlayer;
import Menu.MainMenu;

import javax.swing.*;

import jdk.jfr.internal.tool.Main;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.awt.*;

import static headers.Windows.SIZE_SMALL_WINDOW_X;
import static headers.Windows.SIZE_SMALL_WINDOW_Y;

public class Auth extends JFrame {
    private SessionFactory sessionFactory;

    public Auth(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        setTitle("Регистрация");
        setSize(SIZE_SMALL_WINDOW_X, SIZE_SMALL_WINDOW_Y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel("Имя пользователя:");
        JTextField nameField = new JTextField(20);
        JLabel passLabel = new JLabel("Пароль:");
        JPasswordField passField = new JPasswordField(20);

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(passLabel);
        inputPanel.add(passField);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel infoLabel = new JLabel("");
        infoPanel.add(infoLabel);

        JPanel buttonPanel = new JPanel();
        JButton authButton = new JButton("Авторизоваться");
        buttonPanel.add(authButton);

        mainPanel.add(inputPanel);
        mainPanel.add(infoPanel);
        mainPanel.add(buttonPanel);

        add(mainPanel);

        authButton.addActionListener(e -> {
            String username = nameField.getText();
            String password = new String(passField.getPassword());
            int id = authenticateUser(username, password);

            if (username.isEmpty() || password.isEmpty()) {
                infoLabel.setText("<html><font color='red'>Поля не могут быть пустыми</font></html>");
                return;
            }

            if (username.length() > 30 || password.length() > 30) {
                infoLabel.setText("<html><font color='red'>Слишком длинные данные</font></html>");
                return;
            }

            if (id == -1) {
                infoLabel.setText("<html><font color='red'>Неверное имя пользователя или пароль</font></html>");
                return;
            }
            System.out.println(id);
            JOptionPane.showMessageDialog(Auth.this, "Аутентификация прошла успешно!");
            dispose();

            MainMenu mainMenu = new MainMenu();
            mainMenu.setSessionFactory(sessionFactory, id);
            mainMenu.setVisible(true);
            dispose();
        });

        add(mainPanel);
        setVisible(true);
    }
    private int authenticateUser(String username, String password) {
        try (Session session = sessionFactory.openSession()) {
            Query<InfoPlayer> query = session.createQuery("FROM InfoPlayer WHERE username = :username", InfoPlayer.class);
            query.setParameter("username", username);
            InfoPlayer user = query.uniqueResult();

            if (user == null || !user.getPassword().equals(password)) {
                return -1;
            }

            return user.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
