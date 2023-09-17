package GUI.TableUtils.Webcam.WebcamManager.Events;

import Connections.Client;
import GUI.TableUtils.Webcam.WebcamManager.WebcamGUI;
import org.json.JSONObject;

import java.io.IOException;

public class StopWebcamEvent extends WebcamEvent {
    public StopWebcamEvent(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "STOP_WEBCAM");
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
