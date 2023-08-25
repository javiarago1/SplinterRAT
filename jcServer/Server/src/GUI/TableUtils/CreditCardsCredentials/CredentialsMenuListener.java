package GUI.TableUtils.CreditCardsCredentials;

import Connections.ClientHandler;
import GUI.TableUtils.Configuration.GetSYS;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CredentialsMenuListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        ClientHandler clientHandler = GetSYS.getClientHandler();
        assert clientHandler != null;
        new CredentialsManagerGUI(clientHandler);
    }
}
