package GUI.TableUtils.FileManager.Events;

import GUI.TableUtils.FileManager.FileManagerGUI;
import org.json.JSONObject;

import java.util.List;

public class DeleteFileManagerEvent extends FileManagerEvent {

    public DeleteFileManagerEvent(FileManagerGUI fileManagerGUI, List<String> CMElements) {
        super(fileManagerGUI, CMElements);
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
