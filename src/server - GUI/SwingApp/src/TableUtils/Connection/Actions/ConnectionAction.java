package TableUtils.Connection.Actions;

import Server.Client;
import Utilities.GetSYS;
import TableUtils.Connection.Constants.ConnStatus;
import TableUtils.Connection.Events.ConnectionEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionAction implements ActionListener {

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
