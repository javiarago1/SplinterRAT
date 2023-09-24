package TableUtils.Configuration;

import Server.Client;
import Utilities.GetSYS;
import Main.Main;
import TableUtils.Connection.Actions.ConnectionAction;
import TableUtils.Connection.Constants.ConnStatus;
import TableUtils.KeyLogger.Actions.KeyLoggerAction;
import TableUtils.KeyLogger.Constants.KeyLog;
import TableUtils.KeyboardController.Listeners.KeyboardControllerMenuListener;
import TableUtils.MessageBox.Listeners.MessageBoxMenuListener;
import TableUtils.ScreenStreaming.Listeners.ScreenMenuListener;
import TableUtils.WebcamManager.Listeners.WebcamMenuListener;
import Main.SplinterGUI;
import TableUtils.Credentials.Listeners.CredentialsMenuListener;
import TableUtils.FileManager.Listeners.FileManagerMenuListener;

import TableUtils.Permissions.PermissionsAction;
import TableUtils.ReverseShell.Listeners.ReverseShellMenuListener;
import TableUtils.SystemState.Constants.SystemStatus;
import TableUtils.SystemState.Actions.SystemStateAction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
       // removeRowItem.addActionListener(e -> getConnectionsDefaultTableModel().removeRow(maingetConnectionsTable().getSelectedRow()));
        disconnectedPopUpMenu.add(updateItem);
        updateItem.addActionListener(e -> refreshClient()
        );
    }

    private void refreshClient() {
        javax.swing.table.TableModel tableModel = Main.gui.getConnectionsTable().getModel();
        int row = Main.gui.getConnectionsTable().getSelectedRow();
       // String UUID = (String) maingetConnectionsDefaultTableModel().getValueAt(row, 0);
       /* for (Map.Entry<String, ClientHandler> entry : Main.Main.server.getMap().entrySet())
            if ((UUID).equals(entry.getKey())) {
                maingetConnectionsDefaultTableModel().setValueAt("Connected", row, tableModel.getColumnCount() - 1);
            }*/
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


       // JTable connectionsTable = maingetConnectionsTable();
        // set actions
        webcamMenu.addActionListener(new WebcamMenuListener(mainGUI));
        fileManagerMenu.addActionListener(new FileManagerMenuListener(mainGUI));
        reverseShellMenu.addActionListener(new ReverseShellMenuListener(mainGUI));
        credentialsManagerMenu.addActionListener(new CredentialsMenuListener());
        dumpLogsMenu.addActionListener(new KeyLoggerAction(KeyLog.DUMP_LAST));
        dumpAllLogsMenu.addActionListener(new KeyLoggerAction(KeyLog.DUMP_ALL));
        keyboardController.addActionListener(new KeyboardControllerMenuListener(mainGUI));
        elevatePrivilegesMenu.addActionListener(new PermissionsAction());
        messageBoxMenu.addActionListener(new MessageBoxMenuListener());
        streamScreenMenu.addActionListener(new ScreenMenuListener());
        restartMenu.addActionListener(new ConnectionAction(ConnStatus.RESTART));
        disconnectMenu.addActionListener(new ConnectionAction(ConnStatus.DISCONNECT));
        uninstallMenu.addActionListener(new ConnectionAction(ConnStatus.UNINSTALL));
        logOffAction.addActionListener(new SystemStateAction(SystemStatus.LOG_OFF));
        shutdownAction.addActionListener(new SystemStateAction(SystemStatus.SHUTDOWN));
        rebootAction.addActionListener(new SystemStateAction(SystemStatus.REBOOT));

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JTable source = (JTable) e.getSource();
            int row = source.rowAtPoint(e.getPoint());
            int column = source.columnAtPoint(e.getPoint());
            if (!source.isRowSelected(row)) source.changeSelection(row, column, false, false);
            DefaultTableModel defaultTableModel = Main.gui.getConnectionsDefaultTableModel();
            if (defaultTableModel.getValueAt(row, defaultTableModel.getColumnCount() - 1).equals("Connected")) {
                connectedPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
                Client client = GetSYS.getClientHandler();
                assert client != null;
                webcamMenu.setVisible(client.getSysInfo().WEBCAM());
                keyloggerMenuOptions.setVisible(client.getSysInfo().KEYLOGGER());
                streamScreenMenu.setVisible(client.getSysInfo().WEBCAM());
            } else disconnectedPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
