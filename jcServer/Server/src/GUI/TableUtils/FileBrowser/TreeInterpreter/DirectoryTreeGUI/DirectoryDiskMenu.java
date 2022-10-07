package GUI.TableUtils.FileBrowser.TreeInterpreter.DirectoryTreeGUI;

import Connections.Streams;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeGUI.DiskMenu;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils.Action;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class DirectoryDiskMenu extends DiskMenu {

    private final Streams stream;
    private final JDialog dialog;
    private final List<String> filesToCopy;
    private final Action action;

    public DirectoryDiskMenu(JMenu browserMenu, Streams stream, JDialog dialog, List<String> filesToCopy, Action action) {
        super(browserMenu, stream);
        this.dialog = dialog;
        this.stream = stream;
        this.filesToCopy = new ArrayList<>(filesToCopy);
        this.action = action;
    }

    @Override
    protected ActionListener generateActionListener(String path) {
        return e -> new DirectoryTreeGUI(path, stream, dialog, filesToCopy, action);
    }

}
