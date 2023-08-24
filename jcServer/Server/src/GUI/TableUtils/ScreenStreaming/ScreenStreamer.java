package GUI.TableUtils.ScreenStreaming;

import Connections.Streams;
import Information.FolderOpener;
import Information.Time;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScreenStreamer implements Runnable {

    private final Streams stream;
    private final JLabel streamingScreenShower;
    private final AtomicBoolean isScreenshot;
    private final AtomicBoolean isRunning;

    private final JDialog screenStreamerDialog;
    private final ScreenStreamingGUI screenStreamingGUI;


    public ScreenStreamer(ScreenStreamingGUI screenStreamingGUI) {
        this.screenStreamingGUI = screenStreamingGUI;
        stream = screenStreamingGUI.getStream();
        streamingScreenShower = screenStreamingGUI.getStreamingScreenShower();
        streamingScreenShower.setText("");
        isScreenshot = screenStreamingGUI.getIsScreenshot();
        screenStreamerDialog = screenStreamingGUI.getDialog();
        isRunning = screenStreamingGUI.getIsRunning();
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
                stream.sendSize(1);
                array = stream.receiveBytes();
                if (isScreenshot.get()) takeScreenshot(array);
                ImageIcon tempIMG = new ImageIcon(array);
                Image img = tempIMG.getImage();
                SwingUtilities.invokeLater(() -> {
                    Image imgScale = img.getScaledInstance(streamingScreenShower.getWidth(), streamingScreenShower.getHeight(), Image.SCALE_SMOOTH);
                    streamingScreenShower.setIcon(new ImageIcon(imgScale));
                });
            }
            stream.sendSize(-1);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }


    private void takeScreenshot(byte[] bytes) {
        System.out.println("take!");
        isScreenshot.set(false);
        String snapshotDirectory = "\\Screen Snapshots\\";
        String path = screenStreamingGUI.getClientHandler().getSessionFolder() + snapshotDirectory +
                "screenshot_" + new Time().getTime() + ".png";
        try {
            FileUtils.writeByteArrayToFile(new File(path), bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FolderOpener.open(path);
    }

}
