package GUI.TableUtils.FileManager.Actions;

import GUI.ProgressBar.UploadProgressBar;
import Information.AbstractAction;

import java.awt.event.ActionEvent;

public class CancelUploadAction extends AbstractAction<UploadProgressBar<?>> {
    public CancelUploadAction(UploadProgressBar<?> guiManager) {
        super(guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //getGUIManager().close();
        //getClient().getExecutor().submit(new CancelUploadEvent());
    }
}
