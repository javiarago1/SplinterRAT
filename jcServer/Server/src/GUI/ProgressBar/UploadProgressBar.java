package GUI.ProgressBar;

import Connections.Client;
import GUI.TableUtils.FileManager.Actions.CancelUploadAction;
import Information.AbstractDialogCreator;
import Information.GUIManagerInterface;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class UploadProgressBar<T extends AbstractDialogCreator> extends Bar<T> implements GUIManagerInterface {
    ;
    private final String ACTION = "Uploading";
    private final T gui;
    private final AtomicBoolean cancellationAtomic;

    public UploadProgressBar(T gui, AtomicBoolean cancellationAtomic) {
        super(gui);
        this.gui = gui;
        this.cancellationAtomic = cancellationAtomic;
        getCancelOperation().addActionListener(new CancelUploadAction(this, cancellationAtomic));
        setProgressBarVisible();
    }

    @Override
    public void updateProgress(int read, boolean isLastPacket) {
        SwingUtilities.invokeLater(() -> {
            if (totalRead == 0) {
                getFileState().set(ACTION);
            }
            totalRead += read;
            getFileStateLabel().setText("KB read: " + totalRead);
            if (isLastPacket) close();
        });
    }

    @Override
    public Client getClient() {
        return gui.getClient();
    }

}
