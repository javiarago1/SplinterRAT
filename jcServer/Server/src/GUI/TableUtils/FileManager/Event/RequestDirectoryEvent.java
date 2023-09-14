package GUI.TableUtils.FileManager.Event;

import Connections.Client;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class RequestDirectoryEvent extends Event{
    public RequestDirectoryEvent(Client client, List<String> CMElements) {
        super(client, CMElements);
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "DIRECTORY");
        jsonObject.put("path", getCMElements().get(0));
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
