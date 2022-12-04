package GUI.TableUtils.ScreenStreaming;

import Connections.Streams;
import Information.Action;
import Information.Time;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Streamer implements Runnable {

    private final Streams stream;
    private final JLabel streamingScreenShower;
    private final AtomicBoolean isScreenshot;
    private final AtomicBoolean isRunning;
    private final ConcurrentLinkedQueue<String> queueOfEvents;
    private final JDialog screenStreamerDialog;

    private final AtomicBoolean controlComputer;

    public Streamer(ScreenStreamingGUI screenStreamingGUI) {
        stream = screenStreamingGUI.getStream();
        streamingScreenShower = screenStreamingGUI.getStreamingScreenShower();
        streamingScreenShower.setText("");
        isScreenshot = screenStreamingGUI.getIsScreenshot();
        queueOfEvents = screenStreamingGUI.getQueueOfEvents();
        screenStreamerDialog = screenStreamingGUI.getDialog();
        isRunning = screenStreamingGUI.getIsRunning();
        controlComputer = screenStreamingGUI.getComputerControl();
    }

    @Override
    public void run() {
        String[] dimensions;
        try {
            stream.sendAction(Screen.STREAM);
            String received = stream.readString();
            dimensions = received.split(",");
            SwingUtilities.invokeLater(() -> screenStreamerDialog.setSize(new Dimension(Integer.parseInt(dimensions[0]) / 2 + 15, Integer.parseInt(dimensions[1]) / 2 + 40)));
            while (isRunning.get()) {
                byte[] array;
                if (queueOfEvents.isEmpty() || !controlComputer.get()) stream.sendString("null");
                else stream.sendString(queueOfEvents.remove());
                array = stream.receiveBytes();
                if (isScreenshot.get()) takeScreenshot(array);
                ImageIcon tempIMG = new ImageIcon(array);
                Image img = tempIMG.getImage();
                SwingUtilities.invokeLater(() -> {
                    Image imgScale = img.getScaledInstance(streamingScreenShower.getWidth(), streamingScreenShower.getHeight(), Image.SCALE_SMOOTH);
                    streamingScreenShower.setIcon(new ImageIcon(imgScale));
                });
            }
            stream.sendString("END");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }


    private void takeScreenshot(byte[] bytes) {
        isScreenshot.set(false);
        String snapshotDirectory = "\\Screen Snapshots\\";
        String path = stream.getSessionFolder() + snapshotDirectory +
                "screenshot_" + new Time().getTime() + ".png";
        try {
            FileUtils.writeByteArrayToFile(new File(path), bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
