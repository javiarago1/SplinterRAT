package GUI.TableUtils.KeyboardController;

import Connections.Streams;
import GUI.JsGUI;
import GUI.Main;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.KeyboardController.KeyboardControllerGUI;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class KeyboardControllerMenuListener implements ActionListener {

    private final ConcurrentHashMap<Socket, Streams> map;
    private final JTable table;
    private final JsGUI mainGUI;

    public KeyboardControllerMenuListener(JTable table, ConcurrentHashMap<Socket, Streams> map, JsGUI mainGUI) {
        this.mainGUI = mainGUI;
        this.map = map;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = GetSYS.getStream(map, table);
        assert stream != null;
        new KeyboardControllerGUI(mainGUI.getMainGUI(), stream);
    }
}
