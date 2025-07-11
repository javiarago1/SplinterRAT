package TableUtils.WebcamManager.Events;


import Server.BytesChannel;
import Packets.Identificators.Category;

import TableUtils.WebcamManager.WebcamGUI;
import Utilities.Event.AbstractEventGUI;
import org.json.JSONObject;

import java.io.IOException;


/*
 * Class for starting webcam. Includes:
 * - Sending recording information to client
 */

public class StartWebcamEvent extends AbstractEventGUI<WebcamGUI> {

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
