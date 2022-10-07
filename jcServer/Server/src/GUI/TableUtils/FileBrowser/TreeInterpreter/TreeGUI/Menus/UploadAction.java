package GUI.TableUtils.FileBrowser.TreeInterpreter.TreeGUI.Menus;

import Connections.Streams;
import GUI.ProgressBar.UploadProgressBar;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils.TreeMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;

public class UploadAction extends TreeMenu {

    private final JFileChooser fileChooser = new JFileChooser();


    public UploadAction(JTree tree, Streams stream, JDialog dialog) {
        super(tree, stream, dialog);
        fileChooser.setDialogTitle("Select files");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int returnVal = fileChooser.showOpenDialog(getDialog());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            if ((selectedFiles == null)) {
                System.out.println("File exception");
            } else {
                System.out.println("Selection list -> " + Arrays.toString(selectedFiles));
                getStream().getExecutor().submit(new UploadProgressBar(getDialog(), getStream(), selectedFiles, getSelectedPaths()));
            }
        }


    }

}
