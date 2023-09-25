package TableUtils.ScreenStreaming.Events;

import Server.BytesChannel;
import Packets.Identificators.Category;
import TableUtils.ScreenStreaming.ScreenStreamerGUI;
import Utilities.AbstractEvent;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class StartStreamingEvent extends AbstractEvent<ScreenStreamerGUI> {
    public StartStreamingEvent(ScreenStreamerGUI screenStreamerGUI) {
        super(screenStreamerGUI);
    }

    @Override
    public void run() {
        BytesChannel bytesChannel = getClient().createFileChannel(Category.SCREEN_STREAMING);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "START_SCREEN_STREAMING");
        jsonObject.put("monitor_id", Objects.requireNonNull(getGUIManager().getScreenSelector().getSelectedItem()).toString());
        jsonObject.put("channel_id", bytesChannel.getId());
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }
}
