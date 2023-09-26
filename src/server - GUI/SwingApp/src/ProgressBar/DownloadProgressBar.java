package ProgressBar;
import Server.Client;

import Utilities.AbstractDialogCreator;

import javax.swing.*;


public class DownloadProgressBar<T extends AbstractDialogCreator> extends Bar<T> {

    private final String ACTION = "Downloading";

    private final T gui;


    public DownloadProgressBar(T gui) {
        super(gui);
        this.gui = gui;
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
