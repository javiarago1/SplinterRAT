package TableUtils.Connection.Events;

import Server.Client;
import TableUtils.Connection.Constants.ConnStatus;
import Utilities.Event.AbstractEventNoGUI;
import org.json.JSONObject;

import java.io.IOException;

public class ConnectionEvent extends AbstractEventNoGUI {

    private final ConnStatus connStatus;

    public ConnectionEvent(Client client, ConnStatus connStatus) {
        super(client);
        this.connStatus = connStatus;
    }


    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", connStatus.toString());
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            handleGuiError();
        }
    }
}
