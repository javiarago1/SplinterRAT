package TableUtils.FileManager.Actions;

import ProgressBar.DownloadProgressBar;
import TableUtils.FileManager.Events.DownloadFileManagerEvent;
import TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class DownloadAction extends FileManagerAbstractAction {

    public DownloadAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DownloadProgressBar<FileManagerGUI> downloadProgressBar = new DownloadProgressBar<>(getGUIManager());
        getClient().getExecutor().submit(new DownloadFileManagerEvent(getGUIManager(), downloadProgressBar, getSelectedPaths()));
    }


}
