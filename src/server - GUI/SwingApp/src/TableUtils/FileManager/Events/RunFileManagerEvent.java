package TableUtils.FileManager.Events;

import TableUtils.FileManager.FileManagerGUI;
import org.json.JSONObject;

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
            handleGuiError();
        }
    }
}
