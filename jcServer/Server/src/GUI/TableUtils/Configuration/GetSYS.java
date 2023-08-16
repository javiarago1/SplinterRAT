package GUI.TableUtils.Configuration;

import Connections.ClientHandler;
import Connections.Streams;
import GUI.Main;

import javax.swing.*;
import java.util.Map;


public class GetSYS {

    public static ClientHandler getClientHandler() {
        Map<String, ClientHandler> map = Main.server.getMap();
        JTable table = Main.gui.getConnectionsTable();
        String address = table.getValueAt(table.getSelectedRow(), 0).toString();
        for (String a : map.keySet()) {
            if (a.equals(address)) {
                return map.get(a);
            }
        }
        return null;
    }
}

