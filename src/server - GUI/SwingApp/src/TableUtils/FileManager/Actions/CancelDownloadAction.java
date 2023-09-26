package TableUtils.FileManager.Actions;

import ProgressBar.DownloadProgressBar;
import TableUtils.FileManager.Events.CancelDownloadEvent;
import Utilities.AbstractActionGUI;

import java.awt.event.ActionEvent;

public class CancelDownloadAction extends AbstractActionGUI<DownloadProgressBar<?>> {

    public CancelDownloadAction(DownloadProgressBar<?> downloadProgressBar) {
        super(downloadProgressBar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getGUIManager().closeDialog();
        getClient().getExecutor().submit(new CancelDownloadEvent(getGUIManager()));
    }
}
