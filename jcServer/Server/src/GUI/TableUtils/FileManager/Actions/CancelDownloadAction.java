package GUI.TableUtils.FileManager.Actions;

import GUI.ProgressBar.DownloadProgressBar;
import GUI.TableUtils.FileManager.Events.CancelDownloadEvent;
import Information.AbstractAction;

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
