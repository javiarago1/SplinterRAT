package TableUtils.ScreenStreaming.Events;

import TableUtils.ScreenStreaming.ScreenStreamerGUI;
import Utilities.Event.AbstractEventGUI;
import org.json.JSONObject;

import java.io.IOException;

public class StopStreamingEvent extends AbstractEventGUI<ScreenStreamerGUI> {
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
            handleGuiError();
        }
    }
}
