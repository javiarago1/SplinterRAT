package GUI.TableUtils.Configuration;

import Connections.Client;
import Connections.ClientHandler;
import GUI.SplinterGUI;
import GUI.Main;
import GUI.TableUtils.Connection.DisconnectAction;
import GUI.TableUtils.Connection.RestartAction;
import GUI.TableUtils.Connection.UninstallAction;
import GUI.TableUtils.Credentials.CredentialsMenuListener;
import GUI.TableUtils.FileManager.Listener.FileManagerMenuListener;

import GUI.TableUtils.KeyLogger.KeyLoggerEventsListener;
import GUI.TableUtils.KeyLogger.KeyloggerEvents;
import GUI.TableUtils.KeyboardController.KeyboardControllerMenuListener;
import GUI.TableUtils.MessageBox.MessageBoxMenuListener;
import GUI.TableUtils.Permissions.ElevatePermission.ElevatePermissionAction;
import GUI.TableUtils.ReverseShell.ReverseShellMenuListener;
import GUI.TableUtils.ScreenStreaming.ScreenMenuListener;
import GUI.TableUtils.SystemState.State;
import GUI.TableUtils.SystemState.SystemStateListener;
import GUI.TableUtils.WebcamManager.Listeners.WebcamMenuListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Objects;

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
        javax.swing.table.TableModel tableModel = Main.gui.getConnectionsTable().getModel();
        int row = Main.gui.getConnectionsTable().getSelectedRow();
        String UUID = (String) mainGUI.getConnectionsDefaultTableModel().getValueAt(row, 0);
        for (Map.Entry<String, ClientHandler> entry : Main.server.getMap().entrySet())
            if ((UUID).equals(entry.getKey())) {
                mainGUI.getConnectionsDefaultTableModel().setValueAt("Connected", row, tableModel.getColumnCount() - 1);
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
        JMenuItem credentialsManagerMenu = new JMenuItem("Credentials manager");
        setIconToMenuItem(reverseShellMenu, "shell_icon.png");
        JMenuItem keyboardController = new JMenuItem("Keyboard controller");
        setIconToMenuItem(keyboardController, "keyboard_icon.png");
        JMenuItem messageBoxMenu = new JMenuItem("Message box");
        setIconToMenuItem(messageBoxMenu, "message_box_icon.png");
        keyloggerMenuOptions = new JMenu("Keylogger options");
        setIconToMenuItem(keyloggerMenuOptions, "keylogger_icon.png");
        JMenuItem dumpLogsMenu = new JMenuItem("Dump last log");
        JMenuItem dumpAllLogsMenu = new JMenuItem("Dump all logs");
        keyloggerMenuOptions.add(dumpLogsMenu);
        keyloggerMenuOptions.add(dumpAllLogsMenu);
        JMenu permissionsMenu = new JMenu("Admin privileges");
        setIconToMenuItem(permissionsMenu, "permissions_icon.png");
        JMenuItem elevatePrivilegesMenu = new JMenuItem("Elevate privileges");
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
        connectedPopUpMenu.add(credentialsManagerMenu);
        connectedPopUpMenu.add(keyboardController);
        connectedPopUpMenu.add(streamScreenMenu);
        connectedPopUpMenu.add(messageBoxMenu);
        connectedPopUpMenu.add(keyloggerMenuOptions);
        connectedPopUpMenu.add(permissionsMenu);
        connectedPopUpMenu.add(stateActionsMenu);
        connectedPopUpMenu.add(connectionActionsMenu);


        JTable connectionsTable = mainGUI.getConnectionsTable();
        // set actions
        webcamMenu.addActionListener(new WebcamMenuListener(mainGUI));
        fileManagerMenu.addActionListener(new FileManagerMenuListener(mainGUI));
        reverseShellMenu.addActionListener(new ReverseShellMenuListener(mainGUI));
        credentialsManagerMenu.addActionListener(new CredentialsMenuListener());
        dumpLogsMenu.addActionListener(new KeyLoggerEventsListener(KeyloggerEvents.DUMP_LAST));
        dumpAllLogsMenu.addActionListener(new KeyLoggerEventsListener(KeyloggerEvents.DUMP_ALL));
        keyboardController.addActionListener(new KeyboardControllerMenuListener(mainGUI));
        elevatePrivilegesMenu.addActionListener(new ElevatePermissionAction());
        messageBoxMenu.addActionListener(new MessageBoxMenuListener());
        streamScreenMenu.addActionListener(new ScreenMenuListener());
        restartMenu.addActionListener(new RestartAction());
        disconnectMenu.addActionListener(new DisconnectAction());
        uninstallMenu.addActionListener(new UninstallAction());
        logOffAction.addActionListener(new SystemStateListener(State.LOG_OFF));
        shutdownAction.addActionListener(new SystemStateListener(State.SHUTDOWN));
        rebootAction.addActionListener(new SystemStateListener(State.REBOOT));

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JTable source = (JTable) e.getSource();
            int row = source.rowAtPoint(e.getPoint());
            int column = source.columnAtPoint(e.getPoint());
            if (!source.isRowSelected(row)) source.changeSelection(row, column, false, false);
            DefaultTableModel defaultTableModel = mainGUI.getConnectionsDefaultTableModel();
            if (defaultTableModel.getValueAt(row, defaultTableModel.getColumnCount() - 1).equals("Connected")) {
                connectedPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
                Client client = GetSYS.getClientHandlerV2();
                assert client != null;
                webcamMenu.setVisible(client.getSysInfo().WEBCAM());
                keyloggerMenuOptions.setVisible(client.getSysInfo().KEYLOGGER());
                streamScreenMenu.setVisible(client.getSysInfo().WEBCAM());
            } else disconnectedPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
