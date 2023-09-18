package GUI.TableUtils.ScreenStreaming.Events;

import GUI.TableUtils.ScreenStreaming.ScreenStreamerGUI;
import Information.AbstractEvent;
import org.json.JSONObject;

import java.io.IOException;

public class StopStreamingEvent extends AbstractEvent<ScreenStreamerGUI> {
    public StopStreamingEvent(ScreenStreamerGUI screenStreamerGUI) {
        super(screenStreamerGUI);
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "STOP_SCREEN_STREAMING");
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
