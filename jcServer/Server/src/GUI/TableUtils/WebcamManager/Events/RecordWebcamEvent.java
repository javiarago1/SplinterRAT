package GUI.TableUtils.WebcamManager.Events;

import GUI.TableUtils.WebcamManager.WebcamGUI;
import org.json.JSONObject;

import java.io.IOException;

public class RecordWebcamEvent extends WebcamEvent {
    public RecordWebcamEvent(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "START_RECORDING_WEBCAM");
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
