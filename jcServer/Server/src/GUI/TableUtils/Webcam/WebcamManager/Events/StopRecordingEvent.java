package GUI.TableUtils.Webcam.WebcamManager.Events;

import GUI.TableUtils.Webcam.WebcamManager.WebcamGUI;
import org.json.JSONObject;

import java.io.IOException;

public class StopRecordingEvent extends WebcamEvent {
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
