package TableUtils.Permissions.Actions;

import Server.Client;
import TableUtils.Permissions.Events.PrivilegesElevatorEvent;
import Utilities.Action.AbstractActionNoGUI;
import Utilities.GetSYS;

import java.awt.event.ActionEvent;

public class PermissionsAction extends AbstractActionNoGUI {

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        if (client != null)
            client.getExecutor().submit(new PrivilegesElevatorEvent(client));
    }
}
