package GUI.TableUtils.ReverseShell;

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
        Streams stream = Objects.requireNonNull(GetSYS.getStream(SocketType.REVERSE_SHELL));
        ClientHandler clientHandler = GetSYS.getClientHandler();
        assert clientHandler != null;
        new ReverseShellGUI(stream,clientHandler,  mainGUI.getMainGUI());
    }
}
