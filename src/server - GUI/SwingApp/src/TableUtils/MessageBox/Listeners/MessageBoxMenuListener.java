package TableUtils.MessageBox.Listeners;

import Server.Client;
import Utilities.GetSYS;
import TableUtils.MessageBox.MessageBoxGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageBoxMenuListener implements ActionListener {

    public MessageBoxMenuListener() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        assert client != null;
        new MessageBoxGUI(client);
    }
}