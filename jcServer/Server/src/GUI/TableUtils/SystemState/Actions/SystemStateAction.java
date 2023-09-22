package GUI.TableUtils.SystemState.Actions;

import Connections.Client;
import Connections.ClientErrorHandler;
import Connections.Streams;
import Connections.GetSYS;
import GUI.TableUtils.Configuration.SocketType;
import GUI.TableUtils.SystemState.Constants.SystemStatus;
import GUI.TableUtils.SystemState.Events.SystemStateEvent;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class SystemStateAction implements ActionListener {
    private final SystemStatus systemStatus;

    public SystemStateAction(SystemStatus systemStatus) {
        this.systemStatus = systemStatus;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandlerV2();
        assert client != null;
        client.getExecutor().submit(new SystemStateEvent(client, systemStatus));
    }
}
