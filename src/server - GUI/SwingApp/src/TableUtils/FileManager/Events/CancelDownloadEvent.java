package TableUtils.FileManager.Events;

import ProgressBar.DownloadProgressBar;
import Utilities.AbstractEvent;
import org.json.JSONObject;

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
