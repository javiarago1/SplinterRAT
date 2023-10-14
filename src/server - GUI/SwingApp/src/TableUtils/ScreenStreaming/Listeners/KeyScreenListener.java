package TableUtils.ScreenStreaming.Listeners;

import TableUtils.ScreenStreaming.ScreenStreamerGUI;

import org.json.JSONObject;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyScreenListener extends KeyAdapter {

    private final ScreenStreamerGUI screenStreamerGUI;

    public KeyScreenListener(ScreenStreamerGUI screenStreamerGUI) {
        this.screenStreamerGUI = screenStreamerGUI;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        JSONObject keyEvent = new JSONObject();
        keyEvent.put("keyEvent", e.getKeyChar());
        screenStreamerGUI.getQueueOfEvents().add(keyEvent.toString());
    }
}
