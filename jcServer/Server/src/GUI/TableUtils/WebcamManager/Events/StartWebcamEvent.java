package GUI.TableUtils.WebcamManager.Events;


import Connections.BytesChannel;
import Connections.Category;

import GUI.TableUtils.WebcamManager.WebcamGUI;
import org.json.JSONObject;

import java.io.IOException;


/*
 * Class for starting webcam. Includes:
 * - Sending recording information to client
 */

public class StartWebcamEvent extends WebcamEvent {

    public StartWebcamEvent(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }


    @Override
    public void run() {
        // Saving configuration to send it to client
        BytesChannel bytesChannel = getClient().createFileChannel(Category.WEBCAM_STREAMING);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "START_WEBCAM");
        jsonObject.put("selected_device", getGUIManager().getSelectedDevice());
        jsonObject.put("is_fragmented", getGUIManager().isFragmented());
        jsonObject.put("fps", getGUIManager().getFPS());
        jsonObject.put("channel_id", bytesChannel.getId());
        System.out.println("Channel de webcam " + bytesChannel.getId());
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
