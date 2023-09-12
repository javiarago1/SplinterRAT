package Connections;

import GUI.Main;
import Information.NetworkInformation;
import Information.SystemInformation;
import org.eclipse.jetty.server.session.Session;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Client {
    private Session session;

    public Client(Session session) {
        this.session = session;
    }

    public void addRowOfNewConnection(String message) {
        SystemInformation sysInfo = null;
        NetworkInformation netInfo = null;
        SwingUtilities.invokeLater(() -> {
            TableModel tableModel = Main.gui.getConnectionsTable().getModel();
            /*int existingClientRow = getPositionOfExisting(identifier, tableModel); // position if exists in JTable
            if (existingClientRow != -1) {  // change state of connection
                tableModel.setValueAt("Connected", existingClientRow, tableModel.getColumnCount() - 1);
            } else { // new connection*/
            String[] tableRow = new String[]{
                    sysInfo.UUID(),
                    netInfo.USER_COUNTRY(),
                    sysInfo.TAG_NAME(),
                    sysInfo.USER_NAME(),
                    sysInfo.OPERATING_SYSTEM(),
                    "Connected"
            };
            DefaultTableModel defaultTableModel = (DefaultTableModel) tableModel;
            defaultTableModel.addRow(tableRow);
            // }
            //if (ServerGUI.isNotifications() && SystemTray.isSupported())
            //  displayTray(netInfo.IP(), sysInfo.OPERATING_SYSTEM());
            Main.gui.updateNumOfConnectedClients();
        });
    }

    public void processMessage(String message) {
        JSONObject object = new JSONObject();
        switch (object.get("ACTION").toString()) {
            case "NET_SYS_INFO" -> {
                addRowOfNewConnection(message);
            }
            default -> {
                System.out.println(message);
            }
        }
    }

    public void processMessage(byte[] message) {

    }
}
