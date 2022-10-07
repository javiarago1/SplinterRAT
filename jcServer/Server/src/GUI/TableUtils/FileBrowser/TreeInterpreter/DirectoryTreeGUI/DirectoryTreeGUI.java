package GUI.TableUtils.FileBrowser.TreeInterpreter.DirectoryTreeGUI;


import Connections.Streams;
import GUI.TableUtils.FileBrowser.TreeInterpreter.DirectoryTreeGUI.Actions.MoveAction;
import GUI.TableUtils.FileBrowser.TreeInterpreter.DirectoryTreeGUI.Actions.PasteAction;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeGUI.TreeGUI;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils.Action;

import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;
import java.util.List;


public class DirectoryTreeGUI extends TreeGUI {

    public DirectoryTreeGUI(String rootName, Streams stream, JDialog dialog, List<String> filesToCopy, Action action) {
        super(rootName, stream, dialog, filesToCopy, action);
    }


    @Override
    protected void createPopUpMenu() {

        JPopupMenu popupMenu = new JPopupMenu();
        switch (getAction()) {
            case COPY -> {
                JMenuItem pasteItem = new JMenuItem("Paste");
                popupMenu.add(pasteItem);
                pasteItem.addActionListener(new PasteAction(getTree(), getStream(), getFilesArray()));
            }
            case MOVE -> {
                getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                JMenuItem moveItem = new JMenuItem("Move");
                popupMenu.add(moveItem);
                moveItem.addActionListener(new MoveAction(getTree(), getStream(), getFilesArray()));
            }
        }
        JMenuItem exitItem = new JMenuItem("Exit");
        popupMenu.add(exitItem);
        exitItem.addActionListener(e -> getDialog().dispose());
        getTree().addMouseListener(new DirectoryTreeListener(getTree(), popupMenu));
    }


}
