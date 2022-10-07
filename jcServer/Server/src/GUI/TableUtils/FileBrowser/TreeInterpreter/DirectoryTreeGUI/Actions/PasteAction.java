package GUI.TableUtils.FileBrowser.TreeInterpreter.DirectoryTreeGUI.Actions;

import Connections.Streams;
import GUI.TableUtils.FileBrowser.TreeInterpreter.DirectoryTreeGUI.Menus.CopyFiles;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils.TreeMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class PasteAction extends TreeMenu {

    private final List<String> filesToCopy;

    public PasteAction(JTree tree, Streams stream, List<String> filesToCopy) {
        super(tree, stream);
        this.filesToCopy = new ArrayList<>(filesToCopy);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getStream().getExecutor().submit(new CopyFiles(filesToCopy, getSelectedPaths(), getStream()));
    }
}
