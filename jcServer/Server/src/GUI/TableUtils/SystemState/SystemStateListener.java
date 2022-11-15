package GUI.TableUtils.SystemState;

import Connections.ClientErrorHandler;
import Connections.Streams;
import GUI.TableUtils.Configuration.GetSYS;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class SystemStateListener implements ActionListener {
    private final ConcurrentHashMap<Socket, Streams> map;
    private final JTable table;
    private final State state;

    public SystemStateListener(JTable table, ConcurrentHashMap<Socket, Streams> map, State state) {
        this.map = map;
        this.table = table;
        this.state = state;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = GetSYS.getStream(map, table);
        assert stream != null;
        stream.getExecutor().submit(() -> {
            try {
                stream.stateAction(state);
            } catch (IOException ex) {
                new ClientErrorHandler("Unable to change state of client, connection lost.",
                        stream.getClientSocket());
            }
        });
    }
}
