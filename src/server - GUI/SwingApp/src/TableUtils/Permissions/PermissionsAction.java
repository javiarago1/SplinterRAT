package TableUtils.Permissions;

import Server.Client;
import Utilities.GetSYS;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PermissionsAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        assert client != null;
        client.getExecutor().submit(new PrivilegesElevatorEvents(client));
    }
}
