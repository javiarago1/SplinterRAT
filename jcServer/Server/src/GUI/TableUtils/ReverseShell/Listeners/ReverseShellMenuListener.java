package GUI.TableUtils.ReverseShell.Listeners;

import Connections.Client;
import Connections.ClientHandler;
import Connections.Streams;
import GUI.Main;
import GUI.SplinterGUI;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;
import GUI.TableUtils.ReverseShell.ReverseShellGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ReverseShellMenuListener implements ActionListener {

    public ReverseShellMenuListener(SplinterGUI gui) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandlerV2();
        assert client != null;
        ReverseShellGUI reverseShellGUI = new ReverseShellGUI(client);
        client.updater.setReverseShellGUI(reverseShellGUI);
        reverseShellGUI.initializeShell();
    }
}
