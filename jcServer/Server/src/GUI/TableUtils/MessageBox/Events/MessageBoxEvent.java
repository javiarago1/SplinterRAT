package GUI.TableUtils.MessageBox.Events;

import GUI.TableUtils.MessageBox.MessageBoxGUI;
import Information.AbstractEvent;
import org.json.JSONObject;

import java.io.IOException;

public class MessageBoxEvent extends AbstractEvent<MessageBoxGUI> {

    private final JSONObject messageBoxInformation;

    public MessageBoxEvent(MessageBoxGUI messageBoxGUI, JSONObject messageBoxInformation) {
        super(messageBoxGUI);
        this.messageBoxInformation = messageBoxInformation;
    }

    @Override
    public void run() {
        try {
            messageBoxInformation.put("ACTION", "SHOW_MESSAGE_BOX");
            getClient().sendString(messageBoxInformation.toString());
        } catch (IOException e) {
            //new ClientErrorHandler("Unable to open message box, connection lost with client",
            //        messageBoxGUI.getMessageBoxDialog(),
            //        messageBoxGUI.getStream().getClientSocket());
        }
    }
}
