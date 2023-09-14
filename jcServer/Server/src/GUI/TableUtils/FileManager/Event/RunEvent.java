package GUI.TableUtils.FileManager.Event;

import Connections.Client;
import Connections.ClientErrorHandler;
import Connections.Streams;
import Information.Action;
import org.eclipse.jetty.util.ajax.JSON;
import org.json.JSONObject;

import javax.swing.*;
import java.util.List;

public class RunEvent extends Event {

    private final JDialog fileManagerDialog;

    public RunEvent(Client client, List<String> CMElements, JDialog fileManagerDialog) {
        super(client, CMElements);
        this.fileManagerDialog = fileManagerDialog;
    }



    @Override
    public void run() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ACTION", "RUN");
            jsonObject.put("from_paths", getCMElements());
            getClient().sendString(jsonObject.toString());
        } catch (Exception ex) {
            //new ClientErrorHandler("Unable to run, connection lost with client",
            //        fileManagerDialog,
            //        getClient().getClientSocket());
        }
    }
}
