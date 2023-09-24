package GUI.TableUtils.Credentials.Listeners;

import Connections.Client;
import Connections.GetSYS;
import GUI.TableUtils.Credentials.CredentialsManagerGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CredentialsMenuListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        assert client != null;
        CredentialsManagerGUI credentialsManagerGUI = new CredentialsManagerGUI(client);
        client.updater.setCredentialsManagerGUI(credentialsManagerGUI);
    }
}
