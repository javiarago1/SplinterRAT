package GUI.TableUtils.FileBrowser.TreeInterpreter.TreeGUI;


import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class TreeListener implements MouseListener {

    private final JTree tree;
    private final JPopupMenu popupMenu;

    public TreeListener(JTree tree, JPopupMenu popupMenu) {
        this.tree = tree;
        this.popupMenu = popupMenu;

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e) && tree.getSelectionPaths() != null) {
            TreePath[] treePath = tree.getSelectionPaths();

            assert treePath != null;
            if (checkInfo(treePath)) {
                if (treePath.length == 1) {
                    int row = tree.getClosestRowForLocation(e.getX(), e.getY());
                    tree.setSelectionRow(row);
                }
                popupMenu.getComponent(3).setVisible(allFolders(treePath));
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }

    }


    protected boolean allFolders(TreePath[] treePath) {
        for (TreePath e : treePath) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getLastPathComponent();
            if (node.isLeaf()) {
                return false;
            }
        }
        return true;

    }

    protected boolean checkInfo(TreePath[] treePath) {
        for (TreePath e : treePath) {
            Object[] elements = e.getPath();
            String element = elements[elements.length - 1].toString();
            if (element.equals("<EMPTY FOLDER>") || element.equals("<NO MORE FOLDERS>") || element.equals("<LOADING DIRECTORY>") || element.equals("<ACCESS DENIED>")) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public JTree getTree() {
        return tree;
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }
}
