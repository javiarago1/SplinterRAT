package Connections;


import GUI.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.net.Socket;

public class ClientErrorHandler {

    public ClientErrorHandler(String errorMessage, JDialog dialog, Socket clientSocket) {
        removeClientFromMap(clientSocket);
        SwingUtilities.invokeLater(() -> {
            closeDialogOfClient(dialog);
            setDisconnectedUser(clientSocket);
            showErrorMessage(errorMessage);
            
        });
    }

    public ClientErrorHandler(String errorMessage, Socket clientSocket) {
        removeClientFromMap(clientSocket);
        SwingUtilities.invokeLater(() -> {
            setDisconnectedUser(clientSocket);
            showErrorMessage(errorMessage);
        });
    }

    public ClientErrorHandler(String errorMessage, Socket clientSocket, int messageType) {
        removeClientFromMap(clientSocket);
        SwingUtilities.invokeLater(() -> {
            setDisconnectedUser(clientSocket);
            showErrorMessage(errorMessage, messageType);
        });
    }


    public ClientErrorHandler(String errorMessage, int messageType) {
        SwingUtilities.invokeLater(() -> {
            showErrorMessage(errorMessage, messageType);
        });
    }

    private void removeClientFromMap(Socket clientSocket) {
        Main.server.getMap().remove(clientSocket);
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

    private void setDisconnectedUser(Socket clientSocket) {

        DefaultTableModel connectionsDefaultTableModel = Main.gui.getConnectionsDefaultTableModel();
        boolean founded = false;
        for (int i = 0; i < connectionsDefaultTableModel.getRowCount() || !founded; i++) {
            if (Main.gui.getConnectionsTable().getValueAt(i, 0).equals(clientSocket.getInetAddress().toString())) {
                connectionsDefaultTableModel.setValueAt("Disconnected", i, 5);
                founded = true;
            }
        }

    }


}
