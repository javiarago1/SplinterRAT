package TableUtils.FileManager.Events;

import TableUtils.FileManager.FileManagerGUI;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class RequestDiskFileManagerEvent extends FileManagerEvent {
    public RequestDiskFileManagerEvent(FileManagerGUI fileManagerGUI, List<String> CMElements) {
        super(fileManagerGUI, CMElements);
    }

    @Override
    public void run() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ACTION", "DISKS");
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            handleGuiError();
        }
    }
}
