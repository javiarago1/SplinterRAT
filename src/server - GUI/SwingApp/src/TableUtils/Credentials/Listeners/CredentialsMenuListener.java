package TableUtils.Credentials.Listeners;

import Server.Client;
import Utilities.GetSYS;
import TableUtils.Credentials.CredentialsManagerGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Utilities.SwingUpdater;

public class CredentialsMenuListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        assert client != null;
        CredentialsManagerGUI credentialsManagerGUI = new CredentialsManagerGUI(client);
        SwingUpdater swingUpdater = (SwingUpdater) client.updater;
        swingUpdater.setCredentialsManagerGUI(credentialsManagerGUI);
    }
}
