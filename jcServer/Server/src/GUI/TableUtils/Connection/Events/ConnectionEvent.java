package GUI.TableUtils.Connection.Events;

import Connections.Client;
import GUI.TableUtils.Connection.Constants.ConnStatus;
import org.json.JSONObject;

import java.io.IOException;

public class ConnectionEvent implements Runnable {

    private final Client client;
    private final ConnStatus connStatus;

    public ConnectionEvent(Client client, ConnStatus connStatus) {
        this.client = client;
        this.connStatus = connStatus;
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", connStatus.toString());
        try {
            client.sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
