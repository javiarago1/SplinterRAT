package Connections;


import GUI.Main;
import GUI.TableUtils.Configuration.SocketType;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ClientErrorHandler {

    public ClientErrorHandler(String errorMessage, JDialog dialog, Socket clientSocket) {
        String UUID = removeClient(clientSocket);
        SwingUtilities.invokeLater(() -> {
            closeDialogOfClient(dialog);
            setDisconnectedUser(UUID);
            updateNumOfConnected();
            showErrorMessage(errorMessage);
        });
    }

    public ClientErrorHandler(String errorMessage, Socket clientSocket) {
        String UUID = removeClient(clientSocket);
        SwingUtilities.invokeLater(() -> {
            setDisconnectedUser(UUID);
            updateNumOfConnected();
            showErrorMessage(errorMessage);
        });
    }

    public ClientErrorHandler(String errorMessage, Socket clientSocket, int messageType) {
        String UUID = removeClient(clientSocket);
        SwingUtilities.invokeLater(() -> {
            setDisconnectedUser(UUID);
            updateNumOfConnected();
            showErrorMessage(errorMessage, messageType);
        });
    }


    public ClientErrorHandler(String errorMessage, int messageType) {
        SwingUtilities.invokeLater(() -> {
            updateNumOfConnected();
            showErrorMessage(errorMessage, messageType);
        });
    }


    private void updateNumOfConnected() {
        Main.gui.updateNumOfConnectedClients();
    }

    private String removeClient(Socket clientSocket) {
        ConcurrentHashMap<String, ClientHandler> connectionsMap = Main.server.getMap();
        for (String connectionKey : connectionsMap.keySet()) {
            ConcurrentHashMap<SocketType, Streams> streamsMap = connectionsMap.get(connectionKey).getMap();
            for (SocketType streamKey : streamsMap.keySet()) {
                Streams stream = streamsMap.get(streamKey);
                if (stream.getClientSocket() == clientSocket) {
                    connectionsMap.remove(connectionKey);
                    stream.closeStream();
                    return connectionKey;
                }
            }
        }
        System.out.println("Mapa:" + connectionsMap);
        return "";
    }

    private void showErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(Main.gui.getMainGUI(), errorMessage,
                "Exception with client", JOptionPane.ERROR_MESSAGE);
    }

    private void showErrorMessage(String errorMessage, int messageType) {
        JOptionPane.showMessageDialog(Main.gui.getMainGUI(), errorMessage,
                "Client information", messageType);
    }

    private void closeDialogOfClient(JDialog dialog) {
        dialog.dispose();
    }

    private void setDisconnectedUser(String UUID) {
        TableModel connectionsTableModel = Main.gui.getConnectionsTable().getModel();
        boolean founded = false;
        for (int i = 0; i < connectionsTableModel.getRowCount() && !founded; i++) {
            if (connectionsTableModel.getValueAt(i, 0).equals(UUID)) {
                connectionsTableModel.setValueAt("Disconnected", i, connectionsTableModel.getColumnCount() - 1);
                founded = true;
            }
        }

    }


}
