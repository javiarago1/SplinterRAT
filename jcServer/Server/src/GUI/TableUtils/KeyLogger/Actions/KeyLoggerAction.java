package GUI.TableUtils.KeyLogger.Actions;

import Connections.Client;
import Connections.GetSYS;
import GUI.TableUtils.KeyLogger.Constants.KeyLog;
import GUI.TableUtils.KeyLogger.Events.KeyloggerEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KeyLoggerAction implements ActionListener {

    private final KeyLog keyLog;

    public KeyLoggerAction(KeyLog keyLog) {
        this.keyLog = keyLog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandlerV2();
        assert client != null;
        client.getExecutor().submit(new KeyloggerEvent(client, keyLog));
    }
}
