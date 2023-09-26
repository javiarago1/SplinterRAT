package Server;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Main.Main;

public class ServerGUI {

    private final JDialog serverDialog;
    private final GridBagConstraints constraints = new GridBagConstraints();

    private static boolean notifications;


    public ServerGUI(JFrame mainGUI) {
        serverDialog = new JDialog(mainGUI, "Server settings");
        serverDialog.setResizable(false);
        serverDialog.setModal(true);
        serverDialog.setSize(300, 140);
        serverDialog.setLocationRelativeTo(null);
        serverDialog.setLayout(new GridBagLayout());
        addServerConfigurationElements();
        serverDialog.setVisible(true);

    }

    private void addServerConfigurationElements() {
        constraints.insets = new Insets(6, 6, 6, 6);
        JLabel portNumberLabel = new JLabel("Port number to listen: ");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        serverDialog.add(portNumberLabel, constraints);

        // TODO set port form server class configuration
        // Server.getPort()
        JTextField portNumber = new JTextField(String.valueOf(Main.server.getPort()));
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        serverDialog.add(portNumber, constraints);

        JCheckBox notificationCheckBox = new JCheckBox("Popup notification when client connects", notifications);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        notificationCheckBox.addItemListener(e -> notifications = e.getStateChange() == ItemEvent.SELECTED);
        serverDialog.add(notificationCheckBox, constraints);


        JButton stopListeningButton = new JButton("Stop listening");
        // is running?
        stopListeningButton.setEnabled(Main.server.isRunning());
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        serverDialog.add(stopListeningButton, constraints);


        JButton startListeningButton = new JButton("Start listening!");
        startListeningButton.setEnabled(!Main.server.isRunning());
        stopListeningButton.setEnabled(Main.server.isRunning());
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        serverDialog.add(startListeningButton, constraints);
        startListeningButton.addActionListener(e -> {
            if (Main.server.isRunning()) {
                JOptionPane.showMessageDialog(serverDialog, "Server is already running!", "Server error",
                        JOptionPane.WARNING_MESSAGE);
            } else { // checking for valid port
                Pattern pattern = Pattern.compile("([0-9]|[1-9][0-9]{1,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])");
                Matcher matcher = pattern.matcher(portNumber.getText());
                if (matcher.matches()) {
                    int integerPortNumber = Integer.parseInt(portNumber.getText());
                    if (integerPortNumber != Main.server.getPort()) {
                        Main.server.definePort(integerPortNumber);
                    }
                    try {
                        if (Main.server.isRunning()) Main.server.stopServer();
                        Main.server.startServer();
                    } finally {
                        changeColorAndStateOfPortInformation(Main.gui.getListeningPort());
                    }

                    serverDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(serverDialog, "Wrong port try again (1 - 65.536)", "Server error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        stopListeningButton.addActionListener(e -> {
            try {
                if (Main.server.isRunning()) {
                    Main.server.stopServer();
                    JTable mainTable = Main.gui.getConnectionsTable();
                    DefaultTableModel model = (DefaultTableModel) mainTable.getModel();
                    for (int i = 0; i < mainTable.getRowCount(); i++) {
                        model.setValueAt("Disconnected", i, 5);
                    }
                    stopListeningButton.setEnabled(Main.server.isRunning());
                    startListeningButton.setEnabled(!Main.server.isRunning());
                } else {
                    JOptionPane.showMessageDialog(serverDialog, "Server is already stopped!", "Server error",
                            JOptionPane.WARNING_MESSAGE);
                }
            } finally {
                changeColorAndStateOfPortInformation(Main.gui.getListeningPort());
            }
        });

    }

    public static void changeColorAndStateOfPortInformation(JLabel labelOfState) {
        if (Main.server.isRunning()) {
            labelOfState.setText("Listening on port: " + Main.server.getPort());
        } else {
            labelOfState.setText("Server not listening on any port");
        }

    }

    public static boolean isNotifications() {
        return notifications;
    }
}
