package GUI.TableUtils.SystemState;

import Connections.ClientErrorHandler;
import Connections.Streams;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class SystemStateListener implements ActionListener {
    private final State state;

    public SystemStateListener(State state) {

        this.state = state;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = Objects.requireNonNull(GetSYS.getStream(SocketType.MAIN));
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
