package TableUtils.MessageBox.Events;

import TableUtils.MessageBox.MessageBoxGUI;
import Utilities.Event.AbstractEventGUI;
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
            getClient().sendString(messageBoxInformation.toString());
        } catch (IOException e) {
            handleGuiError();
        }
    }
}
