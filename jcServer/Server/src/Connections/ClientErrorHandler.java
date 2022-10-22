package Connections;


import GUI.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.net.Socket;

public class ClientErrorHandler {

    public ClientErrorHandler(String errorMessage, JDialog dialog, Socket clientSocket) {
        Main.gui.getMap().remove(clientSocket);
        SwingUtilities.invokeLater(() -> {
            DefaultTableModel connectionsDefaultTableModel = Main.gui.getConnectionsDefaultTableModel();
            boolean founded = false;
            for (int i = 0; i < connectionsDefaultTableModel.getRowCount() || !founded; i++) {
                if (Main.gui.getConnectionsTable().getValueAt(i, 0).equals(clientSocket.getInetAddress().toString())) {
                    connectionsDefaultTableModel.setValueAt("Disconnected", i, 5);
                    founded = true;
                }
            }
            JOptionPane.showMessageDialog(Main.gui.getMainGUI(), errorMessage,
                    "Exception with client", JOptionPane.ERROR_MESSAGE);
            dialog.dispose();
        });

    }

}
