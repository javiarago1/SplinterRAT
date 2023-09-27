package TableUtils.Permissions.Events;

import Server.Client;
import Utilities.Event.AbstractEventNoGUI;
import org.json.JSONObject;

import java.io.IOException;


public class PrivilegesElevatorEvent extends AbstractEventNoGUI {


    public PrivilegesElevatorEvent(Client client) {
        super(client);
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "ELEVATE_PERMISSIONS");
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            handleGuiError();
        }
    }
}
