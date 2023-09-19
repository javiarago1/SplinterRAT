package GUI.TableUtils.ScreenStreaming.Events;

import GUI.Compiler.FieldListener;
import GUI.TableUtils.ScreenStreaming.ScreenStreamerGUI;
import Information.AbstractEvent;
import Information.FolderOpener;
import Information.Time;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ScreenShotEvent extends AbstractEvent<ScreenStreamerGUI> {
    public ScreenShotEvent(ScreenStreamerGUI guiManager) {
        super(guiManager);
    }

    @Override
    public void run() {
        String snapshotDirectory = "\\Screenshots\\";
        String folderPath = getClient().getSessionFolder() + snapshotDirectory;
        File file = new File(folderPath);
        file.mkdirs();
        String filePath = folderPath + "screenshot_" + new Time().getTime() + ".png";
        try {
            FileUtils.writeByteArrayToFile(new File(filePath), getGUIManager().getLastData());
            FolderOpener.open(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
