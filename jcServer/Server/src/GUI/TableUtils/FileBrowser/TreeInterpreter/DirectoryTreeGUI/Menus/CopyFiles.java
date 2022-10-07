package GUI.TableUtils.FileBrowser.TreeInterpreter.DirectoryTreeGUI.Menus;


import Connections.Streams;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils.Manager;
import Information.Action;

import java.util.List;

public class CopyFiles extends Manager {

    public CopyFiles(List<String> filesToCopy, List<String> directories, Streams stream) {
        super(filesToCopy, directories, stream);
    }

    @Override
    public void run() {
        getStream().sendAndReadJSON(Action.COPY, getFilesArray(), getDirectories());
    }
}
