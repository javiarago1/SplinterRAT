package TableUtils.FileManager.Events;

import TableUtils.FileManager.FileManagerGUI;
import org.json.JSONObject;

import java.util.List;

public class MoveFileManagerEvent extends FileManagerEvent {
    private final String directoryToMove;

    public MoveFileManagerEvent(FileManagerGUI filemanagerGUI, List<String> CMElements, String directoryToMove) {
        super(filemanagerGUI, CMElements);
        this.directoryToMove = directoryToMove;
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
