package GUI.TableUtils.FileBrowser.TreeInterpreter.TreeGUI.Menus;


import Connections.Streams;
import GUI.TableUtils.FileBrowser.TreeInterpreter.DirectoryTreeGUI.DirectoryDiskMenu;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils.Action;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils.TreeMenu;

import javax.swing.*;
import javax.swing.event.MenuEvent;


public class CopyMenu extends TreeMenu {

    private final JDialog dialog;
    private final JMenu copyItem;

    public CopyMenu(JTree tree, Streams stream, JDialog dialog, JMenu copyItem) {
        super(tree, stream);
        this.dialog = dialog;
        this.copyItem = copyItem;
    }

    @Override
    public void menuSelected(MenuEvent e) {
        copyItem.removeAll();
        getStream().getExecutor().submit(new DirectoryDiskMenu(copyItem, getStream(), dialog, getSelectedPaths(), Action.COPY));
    }


}
