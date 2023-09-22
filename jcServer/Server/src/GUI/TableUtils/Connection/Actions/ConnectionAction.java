package GUI.TableUtils.Connection.Actions;

import Connections.Client;
import Connections.GetSYS;
import GUI.TableUtils.Connection.Constants.ConnStatus;
import GUI.TableUtils.Connection.Events.ConnectionEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionAction implements ActionListener {

    private final ConnStatus connStatus;

    public ConnectionAction(ConnStatus connStatus) {
        this.connStatus = connStatus;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandlerV2();
        assert client != null;
        client.getExecutor().submit(new ConnectionEvent(client, connStatus));
    }
}
