package GUI.TableUtils.FileManager.Event;

import Connections.Client;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class RequestDiskEvent extends Event{
    public RequestDiskEvent(Client client, List<String> CMElements) {
        super(client, CMElements);
    }

    @Override
    public void run() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ACTION", "DISKS");
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
