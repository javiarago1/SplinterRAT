package GUI.TableUtils.ScreenStreaming.Listeners;

import GUI.TableUtils.FileManager.FileManagerGUI;
import GUI.TableUtils.ScreenStreaming.Events.SendKeysEvent;
import GUI.TableUtils.ScreenStreaming.ScreenStreamerGUI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class KeyScreenListener extends KeyAdapter {

    private final ScreenStreamerGUI screenStreamerGUI;

    public KeyScreenListener(ScreenStreamerGUI screenStreamerGUI) {
        this.screenStreamerGUI = screenStreamerGUI;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        screenStreamerGUI.getQueueOfEvents().add("key/" + e.getKeyChar());
        screenStreamerGUI.getClient().getExecutor().submit(new SendKeysEvent(screenStreamerGUI));
    }
}
