package GUI.TableUtils.WebcamManager.Events;

import GUI.TableUtils.WebcamManager.WebcamGUI;
import org.json.JSONObject;

import java.io.IOException;


/*
 * Requesting webcam devices through sockets and getting
 * a list containing them.
 */

public class WebcamDevicesEvent extends WebcamEvent {

    public WebcamDevicesEvent(WebcamGUI webcamGUI) {
        super(webcamGUI);

    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "WEBCAM_DEVICES");
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
