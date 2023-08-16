package GUI.TableUtils.ReverseShell;

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

    public ReverseShellMenuListener() {
        this.mainGUI = Main.gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = Objects.requireNonNull(GetSYS.getClientHandler()).getMainStream();
        new ReverseShellGUI(stream, mainGUI.getMainGUI());
    }
}
