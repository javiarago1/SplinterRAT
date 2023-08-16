package GUI.TableUtils.Connection;

import Connections.ClientErrorHandler;
import Connections.Streams;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class UninstallAction implements ActionListener {

    public UninstallAction() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = Objects.requireNonNull(GetSYS.getClientHandler()).getMainStream();
        stream.getExecutor().submit(() -> {
            try {
                stream.sendAction(Connection.UNINSTALL);
            } catch (IOException ex) {
                new ClientErrorHandler("Unable to uninstall, connection lost with client.",
                        stream.getClientSocket());
            }
        });
    }
}