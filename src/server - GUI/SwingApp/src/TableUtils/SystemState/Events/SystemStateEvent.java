package TableUtils.SystemState.Events;

import Server.Client;
import TableUtils.SystemState.Constants.SystemStatus;
import Utilities.AbstractEvent;
import Utilities.AbstractEventNoGUI;
import org.json.JSONObject;

import java.io.IOException;

public class SystemStateEvent extends AbstractEventNoGUI {

    private final Client client;
    private final SystemStatus systemStatus;

    public SystemStateEvent(Client client, SystemStatus systemStatus) {
        super(client);
        this.client = client;
        this.systemStatus = systemStatus;
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "SYSTEM_STATE");
        jsonObject.put("type", systemStatus.getValue());
        try {
            client.sendString(jsonObject.toString());
        } catch (IOException e) {
            handleGuiError();
        }
    }
}
