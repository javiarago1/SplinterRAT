package GUI.TableUtils.FileManager.Events;

import GUI.ProgressBar.DownloadProgressBar;
import GUI.TableUtils.FileManager.FileManagerGUI;
import Information.AbstractEvent;
import Information.GUIManagerInterface;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class CancelDownloadEvent extends AbstractEvent<DownloadProgressBar<?>> {
    public CancelDownloadEvent(DownloadProgressBar<?> downloadProgressBar) {
        super(downloadProgressBar);
    }

    @Override
    public void run() {
        getClient().closeFileChannel(getGUIManager().getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "CANCEL_DOWNLOAD");
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
