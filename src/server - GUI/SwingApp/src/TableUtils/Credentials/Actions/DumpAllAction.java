package TableUtils.Credentials.Actions;


import TableUtils.Credentials.CredentialsManagerGUI;
import TableUtils.Credentials.Events.DumpAllEvent;
import Utilities.Action.AbstractActionGUI;

import java.awt.event.ActionEvent;

public class DumpAllAction extends AbstractActionGUI<CredentialsManagerGUI> {
    public DumpAllAction(CredentialsManagerGUI credentialsManagerGUI) {
        super(credentialsManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getClient().getExecutor().submit(new DumpAllEvent(getGUIManager()));
    }
}
