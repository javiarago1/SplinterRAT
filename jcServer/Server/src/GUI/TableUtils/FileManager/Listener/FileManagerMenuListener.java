package GUI.TableUtils.FileManager.Listener;

import Connections.ClientErrorHandler;
import Connections.Streams;
import GUI.JsGUI;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.FileManager.FileManagerGUI;
import Information.NullStream;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class FileManagerMenuListener implements ActionListener {

    private final ConcurrentHashMap<Socket, Streams> map;
    private final JTable table;

    private final JsGUI mainGUI;

    public FileManagerMenuListener(JTable table, ConcurrentHashMap<Socket, Streams> map, JsGUI mainGUI) {
        this.mainGUI = mainGUI;
        this.map = map;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = null;
        try {
            stream = GetSYS.getStream(map, table);
            new FileManagerGUI(stream, mainGUI.getMainGUI());
        } catch (NullStream ex) {
            new ClientErrorHandler("Error handleling", 2);
        }

    }
}