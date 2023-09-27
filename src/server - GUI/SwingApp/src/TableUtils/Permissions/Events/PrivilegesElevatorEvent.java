package TableUtils.Permissions.Events;

import Server.Client;
import Utilities.Event.AbstractEventNoGUI;
import org.json.JSONObject;

import java.io.IOException;


public class PrivilegesElevatorEvent extends AbstractEventNoGUI {
    private final Client client;

    public PrivilegesElevatorEvent(Client client) {
        super(null);
        this.client = client;
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "ELEVATE_PERMISSIONS");
        try {
            client.sendString(jsonObject.toString());
        } catch (IOException e) {
            handleGuiError();
        }
    }
}
