package TableUtils.WebcamManager.Events;

import TableUtils.WebcamManager.WebcamGUI;
import Utilities.Event.AbstractEventGUI;
import org.json.JSONObject;

import java.io.IOException;

public class StopRecordingEvent extends AbstractEventGUI<WebcamGUI> {
    public StopRecordingEvent(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "STOP_RECORDING_WEBCAM");
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
