package GUI.TableUtils.FileManager.Actions;

import GUI.ProgressBar.DownloadProgressBar;
import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class DownloadAction extends Manager {

    public DownloadAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getFileManagerGUI().getStream().getExecutor().submit(new DownloadProgressBar(getFileManagerGUI().getFileManagerDialog(), getFileManagerGUI().getClientHandler(), getFileManagerGUI().getStream(),getSelectedPaths()));
    }


}
