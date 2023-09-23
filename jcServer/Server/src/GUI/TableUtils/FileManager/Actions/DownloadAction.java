package GUI.TableUtils.FileManager.Actions;

import GUI.ProgressBar.DownloadProgressBar;
import GUI.TableUtils.FileManager.Events.CancelDownloadEvent;
import GUI.TableUtils.FileManager.Events.DownloadFileManagerEvent;
import GUI.TableUtils.FileManager.FileManagerGUI;

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
