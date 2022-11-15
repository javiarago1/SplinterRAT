package GUI.TableUtils.ReverseShell;

import Connections.Streams;
import GUI.JsGUI;
import GUI.Main;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.ScreenStreaming.ScreenStreamingGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ScreenMenuListener implements ActionListener {
    private final ConcurrentHashMap<Socket, Streams> map;
    private final JTable table;


    public ScreenMenuListener(JTable table, ConcurrentHashMap<Socket, Streams> map) {
        this.map = map;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = GetSYS.getStream(map, table);
        assert stream != null;
        new ScreenStreamingGUI(stream);
    }
}
