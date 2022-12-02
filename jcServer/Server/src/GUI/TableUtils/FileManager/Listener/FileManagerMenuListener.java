package GUI.TableUtils.FileManager.Listener;

import Connections.Streams;
import GUI.SplinterGUI;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.FileManager.FileManagerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class FileManagerMenuListener implements ActionListener {

    private final ConcurrentHashMap<Socket, Streams> map;
    private final JTable table;

    private final SplinterGUI mainGUI;

    public FileManagerMenuListener(JTable table, ConcurrentHashMap<Socket, Streams> map, SplinterGUI mainGUI) {
        this.mainGUI = mainGUI;
        this.map = map;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream;
        stream = GetSYS.getStream(map, table);
        assert stream != null;
        new FileManagerGUI(stream, mainGUI.getMainGUI());


    }
}