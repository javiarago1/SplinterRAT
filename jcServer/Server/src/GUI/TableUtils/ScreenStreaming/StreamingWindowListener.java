package GUI.TableUtils.ScreenStreaming;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.atomic.AtomicBoolean;

/*
    Finish running streaming where window is closing without stopping it normally
 */

public class StreamingWindowListener extends WindowAdapter {

    private final AtomicBoolean isRunning;

    public StreamingWindowListener(AtomicBoolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (isRunning.get()) isRunning.set(false);
    }
}
