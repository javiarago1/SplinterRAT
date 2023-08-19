package GUI.TableUtils.Connection;

import Connections.ClientErrorHandler;
import Connections.Streams;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class RestartAction implements ActionListener {


    public RestartAction() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = Objects.requireNonNull(GetSYS.getStream(SocketType.MAIN));
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