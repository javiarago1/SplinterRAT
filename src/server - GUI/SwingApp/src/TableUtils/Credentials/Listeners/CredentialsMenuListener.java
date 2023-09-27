package TableUtils.Credentials.Listeners;

import Server.Client;
import Utilities.GUIFactory;
import Utilities.GetSYS;
import TableUtils.Credentials.CredentialsManagerGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Utilities.MenuListenerClientAssigner;
import Utilities.SwingUpdater;

public class CredentialsMenuListener extends MenuListenerClientAssigner<CredentialsManagerGUI> {


    public CredentialsMenuListener(GUIFactory<CredentialsManagerGUI> guiFactory) {
        super(guiFactory);
    }
}
