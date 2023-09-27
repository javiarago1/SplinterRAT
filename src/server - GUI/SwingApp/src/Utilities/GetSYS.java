package Utilities;

import Server.Client;
import org.eclipse.jetty.websocket.api.Session;

import Main.Main;

import Server.ConnectionStore;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.Map;


public class GetSYS {

    public static Client getClientHandler() {
        Map<Session, Client> map = ConnectionStore.connectionsMap;
        System.out.println(map.size());
        JTable table = Main.gui.getConnectionsTable();
        String address = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
        for (Session a : map.keySet()) {
            Client client = map.get(a);
            System.out.println(client.getUUID() + " ! " + address);
            if (client.getUUID().equals(address)) {
                return client;
            }
        }
        Main.gui.updateUserStateToDisconnected();
        return null;
    }

}

