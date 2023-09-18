package GUI.TableUtils.ScreenStreaming.Events;

import GUI.TableUtils.ScreenStreaming.ScreenStreamerGUI;
import Information.AbstractEvent;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class SendKeysEvent extends AbstractEvent<ScreenStreamerGUI> {
    public SendKeysEvent(ScreenStreamerGUI screenStreamerGUI) {
        super(screenStreamerGUI);
    }

    @Override
    public void run() {
        if (!getGUIManager().getQueueOfEvents().isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ACTION", "KEY_EXECUTION");
            jsonObject.put("key", getGUIManager().getQueueOfEvents().remove());
            try {
                getClient().sendString(jsonObject.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
