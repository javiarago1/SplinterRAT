package TableUtils.MessageBox.Events;

import TableUtils.MessageBox.MessageBoxGUI;
import Utilities.AbstractEventGUI;
import org.json.JSONObject;

import java.io.IOException;

public class MessageBoxEvent extends AbstractEventGUI<MessageBoxGUI> {

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
            handleGuiError();
        }
    }
}
