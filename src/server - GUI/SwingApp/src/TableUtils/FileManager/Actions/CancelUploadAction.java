package TableUtils.FileManager.Actions;

import ProgressBar.UploadProgressBar;
import TableUtils.FileManager.Events.CancelUploadEvent;
import Utilities.AbstractAction;

import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public class CancelUploadAction extends AbstractAction<UploadProgressBar<?>> {
    private final AtomicBoolean cancellationAtomic;

    public CancelUploadAction(UploadProgressBar<?> guiManager, AtomicBoolean cancellationAtomic) {
        super(guiManager);
        this.cancellationAtomic = cancellationAtomic;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getGUIManager().close();
        getClient().getExecutor().submit(new CancelUploadEvent(getGUIManager(), cancellationAtomic));
    }
}
