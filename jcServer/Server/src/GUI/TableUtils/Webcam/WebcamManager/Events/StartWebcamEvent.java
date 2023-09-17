package GUI.TableUtils.Webcam.WebcamManager.Events;


import Connections.BytesChannel;
import Connections.Category;

import GUI.TableUtils.Webcam.WebcamManager.WebcamGUI;
import org.json.JSONObject;

import java.io.IOException;


/*
 * Class for starting webcam. Includes:
 * - Thread for receiving images and actions (booleans)
 * - Sending recording information to client
 */

public class StartWebcamEvent extends WebcamEvent {

    public StartWebcamEvent(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }


    @Override
    public void run() {
        // Saving configuration to send it to client
        BytesChannel bytesChannel = getClient().createFileChannel(Category.WEBCAM_STREAMING);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "START_WEBCAM");
        jsonObject.put("selected_device", getGUIManager().getSelectedDevice());
        jsonObject.put("is_fragmented", getGUIManager().isFragmented());
        jsonObject.put("fps", getGUIManager().getFPS());
        jsonObject.put("channel_id", bytesChannel.getId());
        System.out.println("Channel de webcam " + bytesChannel.getId());
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startWebcam() {

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
