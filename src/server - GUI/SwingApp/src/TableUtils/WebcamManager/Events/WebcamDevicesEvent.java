package TableUtils.WebcamManager.Events;

import TableUtils.WebcamManager.WebcamGUI;
import Utilities.Event.AbstractEventGUI;
import org.json.JSONObject;

import java.io.IOException;


/*
 * Requesting webcam devices through sockets and getting
 * a list containing them.
 */

public class WebcamDevicesEvent extends AbstractEventGUI<WebcamGUI> {


    public WebcamDevicesEvent(WebcamGUI guiManager) {
        super(guiManager);
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
