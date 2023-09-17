package GUI.TableUtils.WebcamManager.Events;

import GUI.TableUtils.WebcamManager.WebcamGUI;
import Information.FolderOpener;
import Information.Time;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class SnapshotWebcamEvent extends WebcamEvent {
    public SnapshotWebcamEvent(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }

    @Override
    public void run() {
        String snapshotDirectory = "\\Webcam Snapshots\\";
        String path = getClient().getSessionFolder() + snapshotDirectory +
                "snapshot_" + new Time().getTime() + "_" + getGUIManager().getSelectedDevice() + ".png";
        try {
            FileUtils.writeByteArrayToFile(new File(path), getGUIManager().getLastFrame());
            FolderOpener.open(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
