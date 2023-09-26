package TableUtils.ScreenStreaming.Events;

import TableUtils.ScreenStreaming.ScreenStreamerGUI;
import Utilities.AbstractEventGUI;
import org.json.JSONObject;

import java.io.IOException;

public class EventListener extends AbstractEventGUI<ScreenStreamerGUI> {
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
            handleGuiError();
        }

    }
}
