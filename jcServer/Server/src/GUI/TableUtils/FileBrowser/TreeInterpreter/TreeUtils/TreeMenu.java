package GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils;

import Connections.Streams;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public abstract class TreeMenu implements ActionListener, MenuListener {

    private final JTree tree;
    private final List<String> filesArray = new ArrayList<>();
    private final Streams stream;

    private JDialog dialog;

    public TreeMenu(JTree tree, Streams stream) {
        this.tree = tree;
        this.stream = stream;
    }

    public TreeMenu(JTree tree, Streams stream, JDialog dialog) {
        this.dialog = dialog;
        this.tree = tree;
        this.stream = stream;
    }

    private static StringBuilder generatePath(Object[] elements) {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            if (i != elements.length - 1) {
                value.append(elements[i]).append("\\");
            } else {
                value.append(elements[i]);
            }
        }
        return value;
    }

    protected static String getSelectedPath(TreePath treePath) {
        if (treePath != null) {
            Object[] elements = treePath.getPath();
            StringBuilder value = generatePath(elements);
            System.out.println("Selected 1 " + value);
            return value.toString();
        }

        return null;
    }


    protected String getSelectedPath() {
        if (tree.getSelectionPath() != null) {
            TreePath treePath = tree.getSelectionPath();
            Object[] elements = treePath.getPath();
            StringBuilder value = generatePath(elements);
            System.out.println("selected 2 " + value);
            return value.toString();
        }
        return null;
    }

    protected List<String> getSelectedPaths() {
        if (tree.getSelectionPaths() != null) {
            filesArray.clear();
            TreePath[] treePath = tree.getSelectionPaths();
            for (TreePath a : treePath) {
                Object[] elements = a.getPath();
                StringBuilder value = generatePath(elements);
                filesArray.add(value.toString());
            }
            return filesArray;
        }
        return null;
    }


    @Override
    public void menuSelected(MenuEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }

    public JDialog getDialog() {
        return dialog;
    }

    public Streams getStream() {
        return stream;
    }
}
