package GUI.TableUtils.FileManager.Actions;

import GUI.ProgressBar.UploadProgressBar;
import Connections.GetSYS;
import GUI.TableUtils.Configuration.SocketType;
import GUI.TableUtils.FileManager.Events.UploadEvent;
import GUI.TableUtils.FileManager.FileManagerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class UploadAction extends FileManagerAbstractAction {


    private final JFileChooser fileChooser = new JFileChooser();

    public UploadAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
        fileChooser.setDialogTitle("Select files");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int returnVal = fileChooser.showOpenDialog(getGUIManager().getFileManagerDialog());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            if ((selectedFiles == null)) {
                System.out.println("File exception");
            } else {
                System.out.println("Selection list -> " + Arrays.toString(selectedFiles));
                AtomicBoolean cancellationAtomic = new AtomicBoolean(true);
                UploadProgressBar<FileManagerGUI> uploadProgressBar = new UploadProgressBar<>(getGUIManager(), cancellationAtomic);
                uploadProgressBar.setProgressBarVisible();
                getClient().getExecutor().submit(new UploadEvent(getGUIManager(), uploadProgressBar, selectedFiles, getSelectedPath(), cancellationAtomic));
            }
        }
    }
}
