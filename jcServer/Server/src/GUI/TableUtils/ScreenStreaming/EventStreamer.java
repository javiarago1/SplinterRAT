package GUI.TableUtils.ScreenStreaming;

import Connections.Client;
import Connections.Streams;
import GUI.TableUtils.Configuration.SocketType;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventStreamer implements Runnable {
    private final ConcurrentLinkedQueue<String> queueOfEvents;

    private final AtomicBoolean isRunning;

    private final AtomicBoolean isControlComputer;

    //private final Client client;

    public EventStreamer(ScreenStreamingGUI screenStreamingGUI) {
        queueOfEvents = screenStreamingGUI.getQueueOfEvents();
        isRunning = screenStreamingGUI.getIsRunning();
        isControlComputer = screenStreamingGUI.getComputerControl();
    }

    @Override
    public void run() {
        while (isRunning.get()) {
            if (!queueOfEvents.isEmpty() && isControlComputer.get()) {
              /*  try {
                    //.sendString(queueOfEvents.remove());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/
            }
        }
        /*try {
            stream.sendString("END");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }
}
