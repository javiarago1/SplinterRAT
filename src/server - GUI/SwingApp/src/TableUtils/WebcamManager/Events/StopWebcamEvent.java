package TableUtils.WebcamManager.Events;

import TableUtils.WebcamManager.WebcamGUI;
import Utilities.AbstractEventGUI;
import org.json.JSONObject;

import java.io.IOException;

public class StopWebcamEvent extends AbstractEventGUI<WebcamGUI> {

    public StopWebcamEvent(WebcamGUI guiManager) {
        super(guiManager);
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
