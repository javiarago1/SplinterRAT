package GUI.TableUtils.Configuration;

import Connections.Client;
import Connections.ClientHandler;
import Connections.Streams;
import GUI.Main;
import org.eclipse.jetty.websocket.api.Session;

import javax.swing.*;
import java.util.Map;


public class GetSYS {

    public static Streams getStream(SocketType socketType) {
        Map<String, ClientHandler> map = Main.server.getMap();
        JTable table = Main.gui.getConnectionsTable();
        String address = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
        for (String a : map.keySet()) {
            if (a.equals(address)) {
                return map.get(a).getStreamByName(socketType);
            }
        }
        return null;
    }

    public static ClientHandler getClientHandler() {
        Map<String, ClientHandler> map = Main.server.getMap();
        JTable table = Main.gui.getConnectionsTable();
        String address = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
        for (String a : map.keySet()) {
            if (a.equals(address)) {
                return map.get(a);
            }
        }
        return null;
    }


    public static Client getClientHandlerV2() {
        Map<Session, Client> map = Main.serverV2.connectionsMap;
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
        return null;
    }

}

