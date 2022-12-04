package GUI.TableUtils.Connection;

import Connections.ClientErrorHandler;
import Connections.Streams;
import GUI.TableUtils.Configuration.GetSYS;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class RestartAction implements ActionListener {

    private final ConcurrentHashMap<Socket, Streams> map;
    private final JTable table;


    public RestartAction(JTable table, ConcurrentHashMap<Socket, Streams> map) {
        this.map = map;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = GetSYS.getStream(map, table);
        assert stream != null;
        stream.getExecutor().submit(() -> {
            try {
                stream.sendAction(Connection.RESTART);
            } catch (IOException ex) {
                new ClientErrorHandler("Unable to restart, connection lost with client.",
                        stream.getClientSocket());
            }
        });
    }
}