package ProgressBar;

import Utilities.AbstractDialogCreator;
import Utilities.ManagerInterface;
import TableUtils.FileManager.Actions.CancelUploadAction;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

import Server.Client;


public class UploadProgressBar<T extends AbstractDialogCreator> extends Bar<T> implements ManagerInterface {
    ;
    private final String ACTION = "Uploading";
    private final T gui;

    public UploadProgressBar(T gui, AtomicBoolean cancellationAtomic) {
        super(gui);
        this.gui = gui;
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
            if (isLastPacket) closeDialog();
        });
    }

    @Override
    public Client getClient() {
        return gui.getClient();
    }

}
