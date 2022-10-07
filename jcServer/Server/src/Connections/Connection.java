package Connections;

import GUI.Main;
import Information.Action;
import Information.NetworkInformation;
import Information.SystemInformation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class Connection implements Runnable {
    private final ServerSocket server;
    private final ExecutorService executor;
    private final ConcurrentHashMap<Socket, Streams> dialog;

    private int tagCounter = 1;

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
                SystemInformation sysInfo = (SystemInformation) stream.sendAndReadJSON(Action.SYS_INFO);
                stream.setTempSystemInformation(sysInfo);
                NetworkInformation netInfo = (NetworkInformation) stream.sendAndReadJSON(Action.NET_INFO);
                stream.setTempNetworkInformation(netInfo);
                String[] row = new String[]{socket.getInetAddress().toString(), netInfo.USER_COUNTRY(), "User " + tagCounter++, sysInfo.USER_NAME(), sysInfo.OPERATING_SYSTEM(), "Connected"};
                SwingUtilities.invokeLater(() -> {
                    DefaultTableModel model = (DefaultTableModel) Main.gui.getConnectionTable().getModel();
                    model.addRow(row);
                });

            }
            executor.submit(new Connection(server, executor, dialog));
        } catch (Exception e) {
            System.out.println("Exception on client");
            e.printStackTrace();
            executor.submit(new Connection(server, executor, dialog));
        }
    }

}