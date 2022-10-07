package GUI.TableUtils.FileBrowser.TreeInterpreter.TreeGUI.Menus;

import Connections.Streams;
import GUI.ProgressBar.DownloadProgressBar;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils.TreeMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class DownloadAction extends TreeMenu {


    public DownloadAction(JTree tree, Streams stream) {
        super(tree, stream);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getStream().getExecutor().submit(new DownloadProgressBar(getDialog(), getStream(), getSelectedPaths()));
    }


}
