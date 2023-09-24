package Connections;


import GUI.Main;
import GUI.TableUtils.Configuration.SocketType;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ClientErrorHandler {




    public ClientErrorHandler(String errorMessage, int messageType) {
        SwingUtilities.invokeLater(() -> {
            updateNumOfConnected();
            showErrorMessage(errorMessage, messageType);
        });
    }

    private void updateNumOfConnected() {
        Main.gui.updateNumOfConnectedClients();
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
