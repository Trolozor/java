package Game;

import Players.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static headers.Windows.SIZE_SMALL_WINDOW_X;
import static headers.Windows.SIZE_SMALL_WINDOW_Y;

public class StopOrContinueWindow extends JFrame {
    private JTextArea infoTextArea;
    private JButton yesButton;
    private JButton noButton;
    private JLabel timerLabel;
    private Client client;
    private boolean isStop;

    public StopOrContinueWindow(String name, boolean isStop) {
        setTitle("Че делаем");
        setSize(SIZE_SMALL_WINDOW_X, SIZE_SMALL_WINDOW_Y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        this.isStop = isStop;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JLabel text = new JLabel();
        if(isStop) {
            text.setText("Игрок " + name + " хочет остановить игру");
        } else {
            text.setText("Игрок " + name + " хочет начать(продолжить) игру");
        }
        mainPanel.add(text, BorderLayout.NORTH);

        infoTextArea = new JTextArea();
        infoTextArea.setEditable(false);
        infoTextArea.setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        timerLabel = new JLabel("Осталось 20 сек");
        timerLabel.setHorizontalAlignment(JLabel.RIGHT);
        mainPanel.add(timerLabel, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        yesButton = new JButton("Да");
        noButton = new JButton("Нет");
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInfoToText(true);
                disableButton();
            }
        });

        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInfoToText(false);
                disableButton();
            }
        });
    }
    public void addInfoToText(boolean chose) {
        if(chose) {
            client.chose(chose, isStop);
        } else {
            client.chose(chose, isStop);
        }
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public void playerNo(String name) {
        infoTextArea.append("Игрок " + name + " против \n");
    }
    public void playerYes(String name) {
        infoTextArea.append("Игрок " + name + " за \n");
    }
    public void disableButton() {
        yesButton.setEnabled(false);
        noButton.setEnabled(false);
    }
    public void updateTimer(int sec) {
        timerLabel.setText("Осталось " + sec + " сек");
    }
}
