package GUI.ProgressBar;

import Connections.Client;
import GUI.TableUtils.FileManager.Actions.CancelDownloadAction;
import GUI.TableUtils.FileManager.FileManagerGUI;
import Information.AbstractDialogCreator;
import Information.GUIManagerInterface;

import javax.swing.*;


public class DownloadProgressBar<T extends AbstractDialogCreator> extends Bar<T> implements GUIManagerInterface {

    private final String ACTION = "Downloading";

    private final T gui;

    public DownloadProgressBar(T gui) {
        super(gui);
        this.gui = gui;
        getCancelOperation().addActionListener(new CancelDownloadAction(this));
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
