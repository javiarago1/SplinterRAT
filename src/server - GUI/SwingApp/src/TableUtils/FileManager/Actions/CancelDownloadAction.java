package TableUtils.FileManager.Actions;

import ProgressBar.DownloadProgressBar;
import TableUtils.FileManager.Events.CancelDownloadEvent;
import Utilities.AbstractAction;

import java.awt.event.ActionEvent;

public class CancelDownloadAction extends AbstractAction<DownloadProgressBar<?>> {

    public CancelDownloadAction(DownloadProgressBar<?> downloadProgressBar) {
        super(downloadProgressBar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getGUIManager().close();
        getClient().getExecutor().submit(new CancelDownloadEvent(getGUIManager()));
    }
}
