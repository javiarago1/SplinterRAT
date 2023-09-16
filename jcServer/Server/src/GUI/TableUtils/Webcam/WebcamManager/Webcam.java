package GUI.TableUtils.Webcam.WebcamManager;


import javax.swing.*;

import Connections.BytesChannel;
import Connections.Category;
import Connections.ClientErrorHandler;
import Information.Action;

import Information.FolderOpener;
import Information.Time;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;


/*
 * Class for starting webcam. Includes:
 * - Thread for receiving images and actions (booleans)
 * - Sending recording information to client
 */

public class Webcam implements Runnable {


    private final WebcamGUI webcamGUI;

    public Webcam(WebcamGUI webcamGUI) {
        this.webcamGUI = webcamGUI;
    }


    @Override
    public void run() {
        // Saving configuration to send it to client
        try {
            startWebcam();
        } catch (IOException e) {
            //  new ClientErrorHandler("Unable to reproduce webcam, connection lost with client",

            //        webcamGUI.getWebcamDialog(), webcamGUI.getStream().getClientSocket());
        }
    }

    private void startWebcam() throws IOException {
        BytesChannel bytesChannel = webcamGUI.getClient().createFileChannel(Category.IMAGE);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "START_WEBCAM");
        jsonObject.put("selected_device", webcamGUI.getSelectedDevice());
        jsonObject.put("is_fragmented", webcamGUI.isFragmented());
        jsonObject.put("fps", webcamGUI.getFPS());
        jsonObject.put("channel_id", bytesChannel.getId());
        webcamGUI.getClient().sendString(jsonObject.toString());
        /*boolean fragmented = webcamGUI.isFragmented();
        int FPS = webcamGUI.getFPS();
        String selectedDevice = webcamGUI.getSelectedDevice();
        int[] dimensions = (int[]) webcamGUI.getStream().sendAndReadAction(Action.START_WEBCAM, selectedDevice,
                fragmented, FPS);
        // Setting dimension of video capture
        webcamGUI.setFrameDimensions(dimensions[0] + 25, dimensions[1] + 75);

        boolean streamingState = true;

        while (streamingState) {
            if (webcamGUI.isStateStreamingButton()) {
                if (webcamGUI.isStateSaveRecordButton()) {
                    webcamGUI.getStream().sendSize(3);
                    webcamGUI.setStateSaveRecordButton(false);
                    saveRecord();
                    receiveFrame();
                } else if (webcamGUI.isStateStartRecordButton()) {
                    webcamGUI.getStream().sendSize(1);
                    receiveFrame();
                } else if (webcamGUI.isStateStopRecordButton()) {
                    webcamGUI.setStateStopRecordButton(false);
                    webcamGUI.getStream().sendSize(2);
                    receiveFrame();
                } else {
                    webcamGUI.getStream().sendSize(0);
                    receiveFrame();
                }
            } else {
                if (webcamGUI.isSaveAndStop()) {
                    webcamGUI.getStream().sendSize(-2);
                    saveRecord();
                } else {
                    webcamGUI.getStream().sendSize(-1);
                }
                streamingState = false;
            }

        }*/
    }

    // Receive record through socket
    private void saveRecord() throws IOException {
       /* String time = new Time().getTime();
        int numOfFragments = webcamGUI.getStream().readSize();
        String recordDirectory = "\\Webcam Records\\";
        String finalPathForWebcamRecords = webcamGUI.getStream().getSessionFolder() + recordDirectory + time;
        for (int i = 0; i < numOfFragments; i++) {
            webcamGUI.getStream().receiveFile(finalPathForWebcamRecords);
            webcamGUI.getStream().sendSize(0);
        }
        FolderOpener.open(finalPathForWebcamRecords);
*/
    }

    // Receive frame through socket
    private void receiveFrame() throws IOException {
  /*      // Receiving files into byte array
        byte[] array = webcamGUI.getStream().receiveBytes();
        // Check if snapshot is needed to be made
        if (webcamGUI.isStateSnapshotButton()) {
            webcamGUI.setStateSnapshotButton(false);
            String snapshotDirectory = "\\Webcam Snapshots\\";
            String path = webcamGUI.getStream().getSessionFolder() + snapshotDirectory +
                    "snapshot_" + new Time().getTime() + "_" + webcamGUI.getSelectedDevice() + ".png";
            try {
                FileUtils.writeByteArrayToFile(new File(path), array);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        // Setting up image size and call to EDT for changing state of JLabel
        ImageIcon tempIMG = new ImageIcon(array);
        // Image newImage = tempIMG.getImage().getScaledInstance(webcamGUI.getWebcamLabel().getWidth(), webcamGUI.getWebcamLabel().getHeight(), Image.SCALE_FAST);
        // is used the other one ->
        SwingUtilities.invokeLater(() -> webcamGUI.getWebcamLabel().setIcon(tempIMG));
    */
    }
}
