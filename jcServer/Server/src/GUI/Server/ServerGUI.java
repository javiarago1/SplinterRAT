package GUI.Server;

import GUI.Compiler.FieldListener;
import GUI.Main;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;


import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ServerGUI {

    private final JDialog serverDialog;
    private final GridBagConstraints constraints = new GridBagConstraints();

    public ServerGUI(JFrame mainGUI) {
        serverDialog = new JDialog(mainGUI, "Server settings");
        serverDialog.setResizable(false);
        serverDialog.setModal(true);
        serverDialog.setSize(300, 125);
        serverDialog.setLocationRelativeTo(null);
        serverDialog.setLayout(new GridBagLayout());
        addServerConfigurationElements();
        serverDialog.setVisible(true);

    }

    private void addServerConfigurationElements() {
        constraints.insets = new Insets(4, 4, 4, 4);
        JLabel portNumberLabel = new JLabel("Port number to listen: ");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        serverDialog.add(portNumberLabel, constraints);


        JTextField portNumber = new JTextField("4040");
        portNumber.addFocusListener(new FieldListener(portNumber, "4040"));
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        serverDialog.add(portNumber, constraints);


        JCheckBox notificationCheckBox = new JCheckBox("Popup notification when client connects");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        serverDialog.add(notificationCheckBox, constraints);


        JButton stopListeningButton = new JButton("Stop listening");
        stopListeningButton.setEnabled(Main.server.isRunning());
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        serverDialog.add(stopListeningButton, constraints);


        JButton startListeningButton = new JButton("Start listening!");
        startListeningButton.setEnabled(!Main.server.isRunning());
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        serverDialog.add(startListeningButton, constraints);
        startListeningButton.addActionListener(e -> {
            Pattern pattern = Pattern.compile("([0-9]|[1-9][0-9]{1,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])");
            Matcher matcher = pattern.matcher(portNumber.getText());
            if (matcher.matches()) {
                int integerPortNumber = Integer.parseInt(portNumber.getText());
                if (integerPortNumber != Main.server.getPort()) {
                    try {
                        Main.server.definePort(integerPortNumber);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                new Thread(() -> {
                    try {
                        Main.server.startServer();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.out.println("finished thread");
                }).start();
                serverDialog.dispose();
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
                    System.out.println("Server is not running!");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

    }


}
