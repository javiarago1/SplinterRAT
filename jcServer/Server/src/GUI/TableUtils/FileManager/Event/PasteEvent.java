package GUI.TableUtils.FileManager.Event;

import Connections.Client;
import Connections.ClientErrorHandler;
import Connections.Streams;
import Information.Action;
import org.json.JSONObject;

import javax.swing.*;
import java.util.List;

public class PasteEvent extends Event {


    private final List<String> listWhereToPaste;
    private JDialog fileManagerDialog;

    public PasteEvent(Client client, List<String> CMElements, List<String> listWhereToPaste, JDialog fileManagerDialog) {
        super(client, CMElements);
        this.listWhereToPaste = listWhereToPaste;
        this.fileManagerDialog = fileManagerDialog;
    }

    @Override
    public void run() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ACTION", "COPY");
            jsonObject.put("from_paths",getCMElements());
            jsonObject.put("to_paths", listWhereToPaste);
            getClient().sendString(jsonObject.toString());
        } catch (Exception ex) {
         //   new ClientErrorHandler("Unable to paste, connection lost with client",
           //         fileManagerDialog, getClient().getClientSocket());
        }
    }
}
