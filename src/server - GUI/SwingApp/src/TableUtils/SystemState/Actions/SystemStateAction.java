package TableUtils.SystemState.Actions;

import Server.Client;
import Utilities.Action.AbstractActionNoGUI;
import Utilities.GetSYS;
import TableUtils.SystemState.Constants.SystemStatus;
import TableUtils.SystemState.Events.SystemStateEvent;


import java.awt.event.ActionEvent;

public class SystemStateAction extends AbstractActionNoGUI {
    private final SystemStatus systemStatus;

    public SystemStateAction(SystemStatus systemStatus) {
        this.systemStatus = systemStatus;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        if (client != null)
            client.getExecutor().submit(new SystemStateEvent(client, systemStatus));
    }
}
