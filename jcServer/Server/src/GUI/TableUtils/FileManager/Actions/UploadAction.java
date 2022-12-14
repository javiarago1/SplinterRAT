package GUI.TableUtils.FileManager.Actions;

import GUI.ProgressBar.UploadProgressBar;
import GUI.TableUtils.FileManager.FileManagerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;

public class UploadAction extends Manager {


    private final JFileChooser fileChooser = new JFileChooser();

    public UploadAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
        fileChooser.setDialogTitle("Select files");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int returnVal = fileChooser.showOpenDialog(getFileManagerGUI().getFileManagerDialog());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            if ((selectedFiles == null)) {
                System.out.println("File exception");
            } else {
                System.out.println("Selection list -> " + Arrays.toString(selectedFiles));
                getFileManagerGUI().getStream().getExecutor().submit(new UploadProgressBar(
                        getFileManagerGUI().getFileManagerDialog(),
                        getFileManagerGUI().getStream(), selectedFiles, getSelectedPath()));
            }
        }
    }
}
