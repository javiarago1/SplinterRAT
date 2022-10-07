package GUI.TableUtils.FileBrowser.TreeInterpreter.TreeGUI.Menus;

import Connections.Streams;
import GUI.TableUtils.FileBrowser.TreeInterpreter.DirectoryTreeGUI.DirectoryDiskMenu;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils.Action;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils.TreeMenu;

import javax.swing.*;
import javax.swing.event.MenuEvent;


public class MoveMenu extends TreeMenu {
    private final JMenu moveMenu;
    private final JDialog dialog;

    public MoveMenu(JTree tree, Streams stream, JDialog dialog, JMenu moveMenu) {
        super(tree, stream);
        this.dialog = dialog;
        this.moveMenu = moveMenu;
    }

    @Override
    public void menuSelected(MenuEvent e) {
        moveMenu.removeAll();
        getStream().getExecutor().submit(new DirectoryDiskMenu(moveMenu, getStream(), dialog, getSelectedPaths(), Action.MOVE));
    }

}
