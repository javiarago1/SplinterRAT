package GUI.TableUtils.FileManager.Actions;

import Connections.Streams;
import GUI.ProgressBar.UploadProgressBar;
import Connections.GetSYS;
import GUI.TableUtils.Configuration.SocketType;
import GUI.TableUtils.FileManager.FileManagerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;

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
                Streams stream = GetSYS.getStream(SocketType.DOWNLOAD_UPLOAD);
                assert stream != null;
                stream.getExecutor().submit(new UploadProgressBar(
                        getGUIManager().getFileManagerDialog(),
                        stream, selectedFiles, getSelectedPath()));
            }
        }
    }
}
