package GUI.TableUtils.ScreenStreaming;

import Connections.Streams;
import GUI.TableUtils.Configuration.SocketType;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventStreamer implements Runnable {
    private final ConcurrentLinkedQueue<String> queueOfEvents;

    private final AtomicBoolean isRunning;

    private final AtomicBoolean isControlComputer;

    private final Streams stream;

    public EventStreamer(ScreenStreamingGUI screenStreamingGUI) {
        stream = screenStreamingGUI.getAuxEventStream();
        queueOfEvents = screenStreamingGUI.getQueueOfEvents();
        isRunning = screenStreamingGUI.getIsRunning();
        isControlComputer = screenStreamingGUI.getComputerControl();
    }

    @Override
    public void run() {
        while (isRunning.get()) {
            if (!queueOfEvents.isEmpty() && isControlComputer.get()) {
                try {
                    stream.sendString(queueOfEvents.remove());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            stream.sendString("END");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
