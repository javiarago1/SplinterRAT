package GUI.TableUtils.FileBrowser.TreeInterpreter.DirectoryTreeGUI;

import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeGUI.TreeListener;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;

public class DirectoryTreeListener extends TreeListener {
    public DirectoryTreeListener(JTree tree, JPopupMenu popupMenu) {
        super(tree, popupMenu);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e) && getTree().getSelectionPaths() != null) {
            TreePath[] treePath = getTree().getSelectionPaths();

            assert treePath != null;
            if (checkInfo(treePath)) {
                if (treePath.length == 1) {
                    int row = getTree().getClosestRowForLocation(e.getX(), e.getY());
                    getTree().setSelectionRow(row);
                }
                getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
            }
        }

    }

}
