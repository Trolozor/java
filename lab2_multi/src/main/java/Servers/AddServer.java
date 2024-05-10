package Servers;

import headers.*;
import Menu.NetworkGameMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddServer extends JDialog implements Windows {

    private String serverAddress;
    private int serverPort;

    public AddServer(NetworkGameMenu networkGameMenu, ServerPanel serverPanel) {
        JDialog dialog = new JDialog(networkGameMenu, "Добавить сервер", true);
        dialog.setLayout(new BorderLayout());

        // Создаем текстовые поля для ввода IP-адреса и порта сервера
        JTextField ipField = new JTextField(20);
        JTextField portField = new JTextField(10);

        // Создаем панель для текстовых полей и меток
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("IP-адрес:"));
        inputPanel.add(ipField);
        inputPanel.add(new JLabel("Порт:"));
        inputPanel.add(portField);

        // Создаем кнопку "Подтвердить"
        JButton confirmButton = new JButton("Подтвердить");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Получаем введенные значения IP-адреса и порта
                serverAddress = ipField.getText().trim();
                try {
                    serverPort = Integer.parseInt(portField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Введите корректный порт", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Добавляем сервер в MultiPlay.Servers.ServerPanel
                serverPanel.addServer(serverAddress, serverPort);

                dialog.dispose();
            }
        });

        // Добавляем текстовые поля и кнопку на панель диалогового окна
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(confirmButton, BorderLayout.SOUTH);

        // Устанавливаем размер и отображаем диалоговое окно
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
