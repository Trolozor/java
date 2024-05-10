package Game;

import Players.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static headers.Windows.SIZE_SMALL_WINDOW_X;
import static headers.Windows.SIZE_SMALL_WINDOW_Y;

public class WinWindow extends JFrame {
    private JTextArea infoTextArea;
    private JButton resetButton;
    private JButton exitButton;
    private Client client;

    public WinWindow(String name, int score, boolean isEquals) {
        setTitle("Победа");
        setSize(SIZE_SMALL_WINDOW_X, SIZE_SMALL_WINDOW_Y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JLabel textwin = new JLabel();
        if(isEquals) {
            textwin.setText("Ничья");
        } else {
            textwin.setText("Победил " + name + " со счетом: " + score);
        }
        mainPanel.add(textwin, BorderLayout.NORTH);

        infoTextArea = new JTextArea();
        infoTextArea.setEditable(false);
        infoTextArea.setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        resetButton = new JButton("Повторим?");
        exitButton = new JButton("Выход");
        buttonPanel.add(resetButton);
        buttonPanel.add(exitButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Добавляем прослушивателей событий кнопок
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInfoToText(true);
                resetButton.setEnabled(false);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInfoToText(false);
                exitButton.setEnabled(false);
            }
        });
    }

    // Метод для добавления информации в текстовое поле
    private void addInfoToText(boolean repeat) {
        if(repeat) {
            client.repeatGame(repeat);
        } else {
            client.repeatGame(repeat);
        }
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public void playerExit(String name) {
        infoTextArea.append("Игрок " + name + " вышел \n");
    }
    public void playerRepeat(String name) {
        infoTextArea.append("Игрок " + name + " готов повторить \n");
    }
}
