package GUI.TableUtils.ScreenStreaming.Events;

import GUI.TableUtils.ScreenStreaming.ScreenStreamerGUI;
import Information.AbstractEvent;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EventListener extends AbstractEvent<ScreenStreamerGUI> {
    public EventListener(ScreenStreamerGUI guiManager) {
        super(guiManager);
    }

    @Override
    public void run() {
        String result;
        try {
            while (!(result = getGUIManager().getQueueOfEvents().take()).equals("END")) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ACTION", "KEY_EXECUTION");
                jsonObject.put("key", result);
                getClient().sendString(jsonObject.toString());
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

    }
}
