package GUI.TableUtils.FileBrowser.TreeInterpreter.DirectoryTreeGUI;


import Connections.Streams;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils.Folder;
import Information.Action;

import javax.swing.*;
import java.util.List;


public class DirectoryFolderRequest extends Folder {
    public DirectoryFolderRequest(JTree tree, Streams stream) {
        super(tree, stream);
    }

    @Override
    public List<String> requestTree(String value) {
        return getStream().sendAndReadJSON(Action.R_FO_DIR, value);
    }
}
