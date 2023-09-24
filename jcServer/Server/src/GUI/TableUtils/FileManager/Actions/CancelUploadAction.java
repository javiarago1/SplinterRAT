package GUI.TableUtils.FileManager.Actions;

import GUI.ProgressBar.UploadProgressBar;
import GUI.TableUtils.FileManager.Events.CancelUploadEvent;
import Information.AbstractAction;

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
