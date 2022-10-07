package GUI.TableUtils.FileBrowser.TreeInterpreter.DirectoryTreeGUI.Actions;

import Connections.Streams;
import GUI.TableUtils.FileBrowser.TreeInterpreter.DirectoryTreeGUI.Menus.MoveFiles;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils.TreeMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class MoveAction extends TreeMenu {

    private final List<String> filesToMove;

    public MoveAction(JTree tree, Streams stream, List<String> filesToMove) {
        super(tree, stream);
        this.filesToMove = new ArrayList<>(filesToMove);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getStream().getExecutor().submit(new MoveFiles(filesToMove, getSelectedPath(), getStream()));
    }
}
