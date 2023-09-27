package TableUtils.FileManager.Events;

import TableUtils.FileManager.FileManagerGUI;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class RequestDirectoryFileManagerEvent extends FileManagerEvent {
    public RequestDirectoryFileManagerEvent(FileManagerGUI fileManagerGUI, List<String> CMElements) {
        super(fileManagerGUI, CMElements);
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "DIRECTORY");
        jsonObject.put("window_id", getGUIManager().getWindowId());
        jsonObject.put("path", getCMElements().get(0));
        try {
            System.out.println(getClient());
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            handleGuiError();
        }
    }
}
