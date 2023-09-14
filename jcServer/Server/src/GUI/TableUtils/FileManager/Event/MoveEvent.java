package GUI.TableUtils.FileManager.Event;

import Connections.Client;
import Connections.ClientErrorHandler;
import Connections.Streams;
import Information.Action;
import org.json.JSONObject;

import javax.swing.*;
import java.util.List;

public class MoveEvent extends Event {
    private final String directoryToMove;
    private final JDialog fileManagerDialog;

    public MoveEvent(Client client, List<String> CMElements, String directoryToMove, JDialog fileManagerDialog) {
        super(client, CMElements);
        this.directoryToMove = directoryToMove;
        this.fileManagerDialog = fileManagerDialog;
    }

    @Override
    public void run() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ACTION", "MOVE");
            jsonObject.put("from_paths", getCMElements());
            jsonObject.put("to_path", directoryToMove);
            getClient().sendString(jsonObject.toString());
        } catch (Exception ex) {
            //new ClientErrorHandler("Unable to move, connection lost with client", fileManagerDialog,
                    //getClient().getClientSocket());
        }
    }
}
