package GUI.TableUtils.FileBrowser.TreeInterpreter.TreeGUI.Actions;

import Connections.Streams;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils.Manager;
import Information.Action;

import java.util.List;

public class DeleteFiles extends Manager {
    public DeleteFiles(List<String> filesArray, Streams stream) {
        super(filesArray, stream);
    }

    @Override
    public void run() {
        getStream().sendAndReadJSON(Action.DELETE, getFilesArray());
    }
}
