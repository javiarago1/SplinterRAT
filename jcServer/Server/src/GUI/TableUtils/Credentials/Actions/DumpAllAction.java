package GUI.TableUtils.Credentials.Actions;


import GUI.TableUtils.Credentials.CredentialsManagerGUI;
import GUI.TableUtils.Credentials.Events.DumpAllEvent;
import Information.AbstractAction;

import java.awt.event.ActionEvent;

public class DumpAllAction extends AbstractAction<CredentialsManagerGUI> {
    public DumpAllAction(CredentialsManagerGUI credentialsManagerGUI) {
        super(credentialsManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getClient().getExecutor().submit(new DumpAllEvent(getGUIManager()));
    }
}
