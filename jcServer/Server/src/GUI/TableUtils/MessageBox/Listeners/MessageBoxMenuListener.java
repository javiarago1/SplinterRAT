package GUI.TableUtils.MessageBox.Listeners;

import Connections.Client;
import Connections.GetSYS;
import GUI.TableUtils.MessageBox.MessageBoxGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageBoxMenuListener implements ActionListener {

    public MessageBoxMenuListener() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandlerV2();
        assert client != null;
        new MessageBoxGUI(client);
    }
}