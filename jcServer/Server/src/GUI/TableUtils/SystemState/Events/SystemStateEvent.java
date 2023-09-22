package GUI.TableUtils.SystemState.Events;

import Connections.Client;
import GUI.TableUtils.SystemState.Constants.SystemStatus;
import org.json.JSONObject;

import java.io.IOException;

public class SystemStateEvent implements Runnable {

    private final Client client;
    private final SystemStatus systemStatus;

    public SystemStateEvent(Client client, SystemStatus systemStatus) {

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
            throw new RuntimeException(e);
        }
    }
}
