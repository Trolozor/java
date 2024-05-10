package Servers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class PanelServer {
    private String serverAddress;
    private int serverPort;
    static private Map<Integer, String> map = new HashMap<>();
    static private Map<Integer, String> mapInfo = new HashMap<>();
    private String serverInfo;
    private int index;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ServerPanel serverPanel;

    public PanelServer(String serverAddress, int serverPort, int index, ServerPanel serverPanel) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.index = index;
        this.serverPanel = serverPanel;

        String temp = serverAddress + ":" + serverPort;
        map.put(index, temp);
        setServerInfo();
        mapInfo.put(index, serverInfo);
        updateInfo();
    }

    public static String getMapAddress(int index) {
        return map.get(index);
    }

    private void updateServerInfo() {
        System.out.println("update start");
        serverPanel.getServerListModel().remove(index);
        serverPanel.getServerListModel().add(index, mapInfo.get(index));
        System.out.println("update end");
    }

    public void setServerInfo() {
        serverInfo = "Сервер - "  + map.get(index) + " / не подключено";
    }

    public String getServerInfo() {
        return serverInfo;
    }

    private void updateInfo() {
        new Thread(() -> {
            try {
                socket = new Socket(serverAddress, serverPort);

                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                System.out.println("create");

                out.writeObject("GetServerInfo");
                System.out.println("get");
                out.flush();

                String[] getServerInfo = (String[]) in.readObject();
                serverInfo = "Сервер - " + getServerInfo[0] + " игроков " + getServerInfo[1] + " из " + getServerInfo[2] + " / " + map.get(index) + " / подключено";
                mapInfo.replace(index, serverInfo);
                System.out.println(mapInfo.get(index));
                updateServerInfo();
                System.out.println("get serverInfo");

                out.close();
                in.close();
                socket.close();
                System.out.println("close");
            } catch (IOException e) {
                e.printStackTrace();
                serverInfo = "Сервер - " + map.get(index) + " / не удалось подключиться";
                mapInfo.replace(index, serverInfo);
                System.out.println(mapInfo.get(index));
                updateServerInfo();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }).start();
    }
}
