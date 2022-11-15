package GUI.TableUtils.ScreenStreaming;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class KeyScreenListener extends KeyAdapter {

    private final ConcurrentLinkedQueue<String> queueOfEvents;
    private final AtomicBoolean computerControl;

    public KeyScreenListener(ConcurrentLinkedQueue<String> queueOfEvents, AtomicBoolean computerControl) {
        this.queueOfEvents = queueOfEvents;
        this.computerControl = computerControl;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (computerControl.get()) queueOfEvents.add("key/" + e.getKeyChar());
    }
}
