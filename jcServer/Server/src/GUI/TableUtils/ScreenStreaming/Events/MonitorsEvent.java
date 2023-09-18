package GUI.TableUtils.ScreenStreaming.Events;

import GUI.TableUtils.ScreenStreaming.ScreenStreamerGUI;
import Information.AbstractEvent;
import Information.GUIManagerInterface;
import org.json.JSONObject;

import java.io.IOException;

public class MonitorsEvent extends AbstractEvent<ScreenStreamerGUI> {
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
            throw new RuntimeException(e);
        }
    }
}
