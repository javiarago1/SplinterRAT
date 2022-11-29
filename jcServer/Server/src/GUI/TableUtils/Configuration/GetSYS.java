package GUI.TableUtils.Configuration;

import Connections.Streams;

import javax.swing.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class GetSYS {

    public static Streams getStream(ConcurrentHashMap<Socket, Streams> map, JTable table) {
        String address = table.getValueAt(table.getSelectedRow(), 0).toString();
        for (Socket a : map.keySet()) {
            if (a.getInetAddress().toString().equals(address)) {
                return map.get(a);
            }
        }
        return null;
    }
}

