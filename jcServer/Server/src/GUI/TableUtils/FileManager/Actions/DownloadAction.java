package GUI.TableUtils.FileManager.Actions;

import GUI.ProgressBar.DownloadProgressBar;
import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class DownloadAction extends FileManagerAbstractAction {

    public DownloadAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getClient().getExecutor().submit(new DownloadProgressBar(getGUIManager().getFileManagerDialog(),
                getGUIManager().getClient(), getSelectedPaths()));
    }


}
