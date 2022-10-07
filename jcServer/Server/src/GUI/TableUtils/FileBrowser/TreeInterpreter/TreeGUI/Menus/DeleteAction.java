package GUI.TableUtils.FileBrowser.TreeInterpreter.TreeGUI.Menus;

import Connections.Streams;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeGUI.Actions.DeleteFiles;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils.TreeMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class DeleteAction extends TreeMenu {
    public DeleteAction(JTree tree, Streams stream) {
        super(tree, stream);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getStream().getExecutor().submit(new DeleteFiles(getSelectedPaths(), getStream()));
    }

}
