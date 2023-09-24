package GUI.TableUtils.ReverseShell.Listeners;

import Connections.Client;
import GUI.SplinterGUI;
import Connections.GetSYS;
import GUI.TableUtils.ReverseShell.ReverseShellGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReverseShellMenuListener implements ActionListener {

    public ReverseShellMenuListener(SplinterGUI gui) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        assert client != null;
        ReverseShellGUI reverseShellGUI = new ReverseShellGUI(client);
        client.updater.setReverseShellGUI(reverseShellGUI);
        reverseShellGUI.initializeShell();
    }
}
