package GUI.TableUtils.FileManager.Event;

import Connections.Client;
import GUI.TableUtils.FileManager.FileManagerGUI;
import org.json.JSONObject;

import javax.swing.*;
import java.util.List;

public class PasteFileManagerEvent extends FileManagerEvent {


    private final List<String> listWhereToPaste;
    private JDialog fileManagerDialog;

    public PasteFileManagerEvent(FileManagerGUI fileManagerGUI, List<String> CMElements, List<String> listWhereToPaste) {
        super(fileManagerGUI, CMElements);
        this.listWhereToPaste = listWhereToPaste;
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
