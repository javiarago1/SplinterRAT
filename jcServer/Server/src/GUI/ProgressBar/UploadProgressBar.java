package GUI.ProgressBar;

import Connections.Client;
import Connections.ClientErrorHandler;
import Connections.ClientHandler;
import Connections.Streams;
import GUI.TableUtils.FileManager.Actions.CancelDownloadAction;
import GUI.TableUtils.FileManager.Actions.CancelUploadAction;
import Information.AbstractDialogCreator;
import Information.Action;
import Information.GUIManagerInterface;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class UploadProgressBar<T extends AbstractDialogCreator> extends Bar<T> implements GUIManagerInterface {
    ;
    private final String ACTION = "Downloading";
    private final T gui;

    public UploadProgressBar(T gui) {
        super(gui);
        this.gui = gui;
        getCancelOperation().addActionListener(new CancelUploadAction(this));
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
