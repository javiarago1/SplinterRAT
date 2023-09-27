package TableUtils.ScreenStreaming.Events;

import TableUtils.ScreenStreaming.ScreenStreamerGUI;
import Utilities.Event.AbstractEventGUI;
import org.json.JSONObject;

import java.io.IOException;

public class MonitorsEvent extends AbstractEventGUI<ScreenStreamerGUI> {
    public MonitorsEvent(ScreenStreamerGUI screenStreamerGUI) {
        super(screenStreamerGUI);
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "MONITORS");
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            handleGuiError();
        }
    }
}
