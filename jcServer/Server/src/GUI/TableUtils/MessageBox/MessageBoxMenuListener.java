package GUI.TableUtils.MessageBox;

import Connections.Streams;
import GUI.JsGUI;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.FileManager.FileManagerGUI;
import GUI.TableUtils.MessageBox.MessageBoxGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class MessageBoxMenuListener implements ActionListener {

    private final ConcurrentHashMap<Socket, Streams> map;
    private final JTable table;

    public MessageBoxMenuListener(JTable table, ConcurrentHashMap<Socket, Streams> map) {
        this.map = map;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = GetSYS.getStream(map, table);
        assert stream != null;
        new MessageBoxGUI(stream);
    }
}