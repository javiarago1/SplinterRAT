package GUI.TableUtils.Permissions.Events;

import Connections.Client;
import org.json.JSONObject;

import java.io.IOException;


public class PrivilegesElevatorEvents implements Runnable {


    private final Client client;

    public PrivilegesElevatorEvents(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "ELEVATE_PERMISSIONS");
        try {
            client.sendString(jsonObject.toString());
        } catch (IOException e) {
            //new ClientErrorHandler("Unable to get privileges, connection lost with client.",
            //        stream.getClientSocket());
        }
    }
}
