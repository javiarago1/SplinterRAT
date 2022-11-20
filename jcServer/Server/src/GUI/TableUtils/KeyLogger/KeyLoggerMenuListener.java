package GUI.TableUtils.KeyLogger;

import Connections.Streams;
import GUI.Main;
import GUI.TableUtils.Configuration.GetSYS;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class KeyLoggerMenuListener implements MenuListener{

    private final ConcurrentHashMap<Socket, Streams> mapOfConnections;
    private final JMenuItem[] keyloggerOptions;

    public KeyLoggerMenuListener(ConcurrentHashMap<Socket,Streams>mapOfConnections, JMenuItem[]keyloggerOptions){
        this.mapOfConnections = mapOfConnections;
        this.keyloggerOptions = keyloggerOptions;
    }
    @Override
    public void menuSelected(MenuEvent e) {
        Streams stream = GetSYS.getStream(mapOfConnections, Main.gui.getConnectionsTable());
        assert stream != null;
        stream.getExecutor().submit(new KeyLoggerStateChecker(stream,keyloggerOptions));

    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }

}