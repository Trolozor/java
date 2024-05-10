package ConnectDB;

import Entity.InfoPlayer;

import javax.swing.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.awt.*;

import static headers.Windows.SIZE_SMALL_WINDOW_X;
import static headers.Windows.SIZE_SMALL_WINDOW_Y;
public class Register extends JFrame {
    private SessionFactory sessionFactory;

    public Register(SessionFactory sessionFactory) {
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

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton registerButton = new JButton("Зарегистрироваться");
        JButton authButton = new JButton("Авторизоваться");
        buttonPanel.add(registerButton);
        buttonPanel.add(authButton);

        mainPanel.add(inputPanel);
        mainPanel.add(infoPanel);
        mainPanel.add(buttonPanel);

        add(mainPanel);

        registerButton.addActionListener(e -> {
            String username = nameField.getText();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                infoLabel.setText("<html><font color='red'>Поля не могут быть пустыми</font></html>");
                return;
            }

            if (username.length() > 30 || password.length() > 30) {
                infoLabel.setText("<html><font color='red'>Слишком длинные данные</font></html>");
                return;
            }

            if (userExists(username)) {
                infoLabel.setText( "<html><font color='red'>Пользователь уже зарегистрирован</font></html>");
                return;
            }


            try {
                InfoPlayer player = new InfoPlayer(username, password, "0");
                saveInfoPlayer(player);
                infoLabel.setText("<html><font color='green'>Пользователь успешно зарегистрирован</font></html>");
            } catch (Exception es) {
                infoLabel.setText("<html><font color='red'>Ошибка регистрации пользователя</font></html>");
                es.printStackTrace();
            }

        });

        authButton.addActionListener(e -> {
            Auth auth = new Auth(sessionFactory);
            auth.setVisible(true);
            dispose();
        });

        add(mainPanel);
        setVisible(true);
    }

    private boolean userExists(String username) {
        Session session = sessionFactory.openSession();
        boolean exists = false;
        try {

            String hql = "SELECT COUNT(*) FROM InfoPlayer WHERE username = :username";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("username", username);
            Long count = query.uniqueResult();

            exists = count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return exists;
    }

    private void saveInfoPlayer(InfoPlayer infoPlayer) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(infoPlayer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}

