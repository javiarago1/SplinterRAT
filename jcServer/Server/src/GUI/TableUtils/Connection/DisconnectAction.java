package GUI.TableUtils.Connection;

import Connections.ClientErrorHandler;
import Connections.Streams;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class DisconnectAction implements ActionListener {


    public DisconnectAction() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = GetSYS.getStream(SocketType.MAIN);
        stream.getExecutor().submit(() -> {
            try {
                stream.sendAction(Connection.DISCONNECT);
            } catch (IOException ex) {
                new ClientErrorHandler("Unable to disconnect, connection lost with client.", stream.getClientSocket());
            }
        });
    }
}