package GUI.TableUtils.FileBrowser.TreeInterpreter.TreeGUI;

import Connections.Streams;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils.Folder;
import Information.Action;

import javax.swing.*;
import java.util.List;

public class FolderRequest extends Folder {
    public FolderRequest(JTree tree, Streams stream) {
        super(tree, stream);
    }

    @Override
    public List<String> requestTree(String value) {
        return getStream().sendAndReadJSON(Action.R_A_DIR, value);
    }
}
