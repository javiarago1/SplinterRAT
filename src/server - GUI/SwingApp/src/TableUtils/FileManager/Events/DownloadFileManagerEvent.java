package TableUtils.FileManager.Events;

import Server.BytesChannel;
import Information.Category;
import ProgressBar.DownloadProgressBar;
import TableUtils.FileManager.FileManagerGUI;
import Utilities.AbstractEvent;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class DownloadFileManagerEvent extends AbstractEvent<FileManagerGUI> {
    private final DownloadProgressBar<FileManagerGUI> downloadProgressBar;
    private final List<String> downloadList;

    public DownloadFileManagerEvent(FileManagerGUI guiManager, DownloadProgressBar<FileManagerGUI> downloadProgressBar, List<String> downloadList) {
        super(guiManager);
        this.downloadProgressBar = downloadProgressBar;
        this.downloadList = downloadList;
    }


    @Override
    public void run() {
        BytesChannel bytesChannel = getClient().createFileChannel(Category.ZIP_FILE);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "DOWNLOAD");
        jsonObject.put("from_path", downloadList);
        jsonObject.put("channel_id", bytesChannel.getId());
        System.out.println("File id de download: " + bytesChannel.getId());
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
