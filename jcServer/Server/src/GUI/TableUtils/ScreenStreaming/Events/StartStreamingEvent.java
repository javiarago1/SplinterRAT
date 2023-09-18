package GUI.TableUtils.ScreenStreaming.Events;

import Connections.BytesChannel;
import Connections.Category;
import GUI.TableUtils.ScreenStreaming.ScreenStreamerGUI;
import Information.AbstractEvent;
import org.json.JSONObject;

import java.io.IOException;

public class StartStreamingEvent extends AbstractEvent<ScreenStreamerGUI> {
    public StartStreamingEvent(ScreenStreamerGUI screenStreamerGUI) {
        super(screenStreamerGUI);
    }

    @Override
    public void run() {
        BytesChannel bytesChannel = getClient().createFileChannel(Category.SCREEN_STREAMING);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "START_SCREEN_STREAMING");
        jsonObject.put("channel_id", bytesChannel.getId());
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }
}
