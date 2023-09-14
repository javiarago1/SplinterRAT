package GUI.TableUtils.FileManager.Event;

import Connections.Client;
import Connections.ClientErrorHandler;
import Connections.Streams;
import Information.Action;
import org.json.JSONObject;

import javax.swing.*;
import java.util.List;

public class DeleteEvent extends Event {

    private final JDialog fileManagerDialog;

    public DeleteEvent(Client client, List<String> CMElements, JDialog fileManagerDialog) {
        super(client, CMElements);
        this.fileManagerDialog = fileManagerDialog;
    }

    @Override
    public void run() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ACTION", "DELETE");
            jsonObject.put("from_paths", getCMElements());
            getClient().sendString(jsonObject.toString());
        } catch (Exception e) {
           // new ClientErrorHandler("Unable to delete, connection lost with client",
           //         fileManagerDialog,
           //         getClient().getClientSocket());
        }
    }
}
