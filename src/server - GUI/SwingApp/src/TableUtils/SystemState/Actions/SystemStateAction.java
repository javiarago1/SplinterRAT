package TableUtils.SystemState.Actions;

import Server.Client;
import Utilities.GetSYS;
import TableUtils.SystemState.Constants.SystemStatus;
import TableUtils.SystemState.Events.SystemStateEvent;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SystemStateAction implements ActionListener {
    private final SystemStatus systemStatus;

    public SystemStateAction(SystemStatus systemStatus) {
        this.systemStatus = systemStatus;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        assert client != null;
        client.getExecutor().submit(new SystemStateEvent(client, systemStatus));
    }
}
