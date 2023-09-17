package GUI.TableUtils.FileManager.Event;

import Connections.Client;
import GUI.TableUtils.FileManager.FileManagerGUI;
import org.json.JSONObject;

import javax.swing.*;
import java.util.List;

public class RunFileManagerEvent extends FileManagerEvent {

    public RunFileManagerEvent(FileManagerGUI fileManagerGUI, List<String> CMElements) {
        super(fileManagerGUI, CMElements);
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
