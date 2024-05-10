package Menu;

import Players.Player;
import headers.*;
import Servers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerNameDialog extends JDialog implements Windows {

    final int SIZE_WINDOW_SMALL_X = Windows.SIZE_WINDOW_X /4;
    final int SIZE_WINDOW_SMALL_Y = Windows.SIZE_WINDOW_Y /6;
    private JTextField playerNameField;
    private JButton confirmButton;


    public PlayerNameDialog(MainMenu mainMenu, boolean isMultiplayer, boolean isHost) {
        super(mainMenu, "Введите имя", true);
        setSize(SIZE_WINDOW_SMALL_X, SIZE_WINDOW_SMALL_Y);
        setLocationRelativeTo(mainMenu);

        playerNameField = new JTextField(15);
        confirmButton = new JButton("Подтвердить");

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(playerNameField);
        panel.add(confirmButton);
        add(panel, BorderLayout.CENTER);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = playerNameField.getText().trim();
                if (playerName.isEmpty() || playerName.length() > 30) {
                    JOptionPane.showMessageDialog(PlayerNameDialog.this, "Введите имя (не более 30 символов).", "Ошибка", JOptionPane.ERROR_MESSAGE);
                } else if(!isMultiplayer) {
                    mainMenu.setVisible(false);
                    dispose();
                    Player player = new Player(playerName);
                    CreateHost createHost = new CreateHost();
                    createHost.setMainMenu(mainMenu);
                    createHost.setPlayer(player);
                    createHost.singleplayer();
                    createHost.setVisible(true);

                } else if(isMultiplayer && !isHost) {
                    mainMenu.setVisible(false);
                    dispose();
                    Player player = new Player(playerName);

                    NetworkGameMenu networkGameMenu = new NetworkGameMenu(mainMenu, player);
                    networkGameMenu.setVisible(true);
                } else if(isMultiplayer && isHost) {
                    mainMenu.setVisible(false);
                    dispose();
                    Player player = new Player(playerName);

                    CreateHost createHost = new CreateHost();
                    createHost.setMainMenu(mainMenu);
                    createHost.setPlayer(player);
                    createHost.multiplayer();
                    createHost.setVisible(true);
                }
            }
        });
    }
}
