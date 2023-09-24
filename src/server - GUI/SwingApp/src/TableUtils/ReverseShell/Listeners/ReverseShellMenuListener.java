package TableUtils.ReverseShell.Listeners;



import Utilities.GetSYS;
import Main.SplinterGUI;
import TableUtils.ReverseShell.ReverseShellGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Server.Client;
import Utilities.SwingUpdater;

public class ReverseShellMenuListener implements ActionListener {

    public ReverseShellMenuListener(SplinterGUI gui) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        assert client != null;
        ReverseShellGUI reverseShellGUI = new ReverseShellGUI(client);
        SwingUpdater swingUpdater = (SwingUpdater) client.updater;
        swingUpdater.setReverseShellGUI(reverseShellGUI);
        reverseShellGUI.initializeShell();
    }
}
