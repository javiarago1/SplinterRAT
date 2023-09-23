package GUI.TableUtils.FileManager.Actions;

import GUI.Compiler.FieldListener;
import GUI.ProgressBar.DownloadProgressBar;
import GUI.TableUtils.FileManager.Events.CancelDownloadEvent;
import GUI.TableUtils.FileManager.FileManagerGUI;
import Information.AbstractAction;

import java.awt.event.ActionEvent;
import java.io.File;

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
