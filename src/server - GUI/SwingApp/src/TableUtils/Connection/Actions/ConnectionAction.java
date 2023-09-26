package TableUtils.Connection.Actions;

import Server.Client;
import Utilities.AbstractActionNoGUI;
import Utilities.GetSYS;
import TableUtils.Connection.Constants.ConnStatus;
import TableUtils.Connection.Events.ConnectionEvent;

import java.awt.event.ActionEvent;

public class ConnectionAction extends AbstractActionNoGUI {

    private final ConnStatus connStatus;

    public ConnectionAction(ConnStatus connStatus) {
        this.connStatus = connStatus;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        assert client != null;
        client.getExecutor().submit(new ConnectionEvent(client, connStatus));
    }
}
