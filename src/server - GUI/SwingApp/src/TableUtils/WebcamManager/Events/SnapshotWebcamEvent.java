package TableUtils.WebcamManager.Events;

import TableUtils.WebcamManager.WebcamGUI;
import Utilities.AbstractEventGUI;
import Utilities.FolderOpener;
import Utilities.Time;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class SnapshotWebcamEvent extends AbstractEventGUI<WebcamGUI> {

    public SnapshotWebcamEvent(WebcamGUI guiManager) {
        super(guiManager);
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
