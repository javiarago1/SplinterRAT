package GUI.TableUtils.FileBrowser.TreeInterpreter.DirectoryTreeGUI.Menus;


import Connections.Streams;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils.Manager;
import Information.Action;

import java.util.List;

public class MoveFiles extends Manager {


    public MoveFiles(List<String> filesToCopy, String directory, Streams stream) {
        super(filesToCopy, directory, stream);
    }

    @Override
    public void run() {
        getStream().sendAndReadJSON(Action.MOVE, getFilesArray(), getDirectory());
    }
}
