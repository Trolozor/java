package Servers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ServerPanel extends JPanel {
    private DefaultListModel<String> serverListModel;
    private JList<String> serverList;
    private PanelServer panelServer;

    public ServerPanel() {
        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout());

        serverListModel = new DefaultListModel<>();
        serverList = new JList<>(serverListModel);
        serverList.addMouseListener(new ServerListMouseListener());
        Font listFont = serverList.getFont();
        serverList.setFont(new Font(listFont.getName(), Font.PLAIN, 32));

        JScrollPane scrollPane = new JScrollPane(serverList);
        add(scrollPane, BorderLayout.CENTER);

    }
    public DefaultListModel<String> getServerListModel() {
        return serverListModel;
    }
    public String getSelectedServer() {
        int selectedIndex = serverList.getSelectedIndex();
        if(selectedIndex != -1) {
            System.out.println(panelServer.getMapAddress(serverList.getSelectedIndex()));
            return panelServer.getMapAddress(serverList.getSelectedIndex());
        } else {
            return null;
        }
    }
    public JList getServerList() {
        return serverList;
    }

    public void addServer(String serverAddress, int serverPort) {
        panelServer = new PanelServer(serverAddress, serverPort, serverListModel.getSize(), this);
        serverListModel.addElement(panelServer.getServerInfo());
    }
    public void removeServer(int index) {
        serverListModel.remove(index);
    }

    private class ServerListMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) { // Обработка двойного клика
                int index = serverList.locationToIndex(e.getPoint());
                if (index != -1) {
                    String serverInfo = serverListModel.getElementAt(index);
                    // Действия при двойном клике на элементе списка
                    JOptionPane.showMessageDialog(ServerPanel.this, "Вы нажали на сервер: " + serverInfo);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }
}
