package GUI.TableUtils.KeyboardController;

import Connections.Streams;
import GUI.Main;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.KeyboardController.KeyboardControllerGUI;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class KeyboardControllerMenuListener implements MenuListener {

    private final ConcurrentHashMap<Socket, Streams> mapOfConnections;
    private final JMenuItem[] keyloggerOptions;

    public KeyboardControllerMenuListener(ConcurrentHashMap<Socket, Streams> mapOfConnections, JMenuItem[] keyloggerOptions) {
        this.mapOfConnections = mapOfConnections;
        this.keyloggerOptions = keyloggerOptions;
    }

    @Override
    public void menuSelected(MenuEvent e) {
        Streams stream = GetSYS.getStream(mapOfConnections, Main.gui.getConnectionsTable());
        assert stream != null;
        //new KeyboardControllerGUI();
    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}
