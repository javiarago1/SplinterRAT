package GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils;

import Connections.Streams;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.nio.file.Path;
import java.util.List;


public abstract class Folder implements TreeWillExpandListener {

    private final Streams stream;
    private final JTree tree;

    public Folder(JTree tree, Streams stream) {
        this.tree = tree;
        this.stream = stream;
    }

    public abstract List<String> requestTree(String value);

    @Override
    public void treeWillExpand(TreeExpansionEvent event) {
        SwingWorker<Void, Void> requestWorker = new SwingWorker<>() {
            private final TreePath treePath = event.getPath();
            private final DefaultMutableTreeNode selectedFolder = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            private final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            private List<String> receivedList;

            @Override
            protected Void doInBackground() {
                String path = TreeMenu.getSelectedPath(treePath);
                receivedList = requestTree(path);
                return null;
            }

            @Override
            protected void done() {
                if (receivedList.size() == 1) {
                    selectedFolder.removeAllChildren();
                    selectedFolder.add(new DefaultMutableTreeNode("<EMPTY FOLDER>"));
                    model.reload(selectedFolder);
                } else if (receivedList.get(0).equals("ACCESS_DENIED")) {
                    selectedFolder.removeAllChildren();
                    selectedFolder.add(new DefaultMutableTreeNode("<ACCESS DENIED>"));
                    model.reload(selectedFolder);
                } else {
                    selectedFolder.removeAllChildren();
                    selectedFolder.add(new DefaultMutableTreeNode("<LOADING DIRECTORY>"));
                    model.reload(selectedFolder);

                    boolean file = false;
                    for (String e : receivedList) {
                        if (e.equals("/")) {
                            file = true;
                        } else {
                            DefaultMutableTreeNode node = new DefaultMutableTreeNode(e);
                            model.insertNodeInto(node, selectedFolder, selectedFolder.getChildCount());
                            if (!file) node.insert(new DefaultMutableTreeNode("<LOADING DIRECTORY>"),
                                    node.getChildCount());
                        }

                    }
                    model.removeNodeFromParent((MutableTreeNode) selectedFolder.getFirstChild());
                }
            }
        };
        stream.getExecutor().submit(requestWorker);

    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent event) {

    }

    public Streams getStream() {
        return stream;
    }
}
