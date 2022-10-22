package GUI.TableUtils.Webcam.WebcamManager;

import Connections.ClientErrorHandler;
import Information.Action;

import javax.swing.*;
import java.io.IOException;
import java.util.List;


/*
 * Requesting webcam devices through sockets and getting
 * a list containing them.
 */
public class WebcamRequester extends SwingWorker<Void, Void> {
    private final WebcamGUI webcamGUI;

    public WebcamRequester(WebcamGUI webcamGUI) {
        this.webcamGUI = webcamGUI;
    }


    private void disableButtons() {
        webcamGUI.getBoxOfDevices().setEnabled(false);
        webcamGUI.getStartButton().setEnabled(false);
        webcamGUI.getRecordButton().setEnabled(false);
        webcamGUI.getSnapshotButton().setEnabled(false);
        webcamGUI.getSaveRecordButton().setEnabled(false);
    }

    private void enableBasicButtons() {
        webcamGUI.getBoxOfDevices().setEnabled(true);
        webcamGUI.getStartButton().setEnabled(true);
        webcamGUI.getRecordButton().setEnabled(false);
        webcamGUI.getSnapshotButton().setEnabled(false);
        webcamGUI.getSaveRecordButton().setEnabled(false);
    }

    private void enableButtons() {
        if (webcamGUI.getBoxOfDevices().getSelectedIndex() != -1) {
            enableBasicButtons();
        } else {
            disableButtons();
        }
    }


    private List<?> listOfWebcams;

    @Override
    protected Void doInBackground() {
        try {
            listOfWebcams = (List<?>) webcamGUI.getStream().sendAndReadJSON(Action.REQUEST_WEBCAM);

        } catch (IOException e) {
            new ClientErrorHandler("Unable to request devices, connection lost with client",
                    webcamGUI.getWebcamDialog(), webcamGUI.getStream().getClientSocket());
        }
        return null;
    }

    @Override
    protected void done() {
        if (listOfWebcams != null) {
            // Case where no devices are found -> all buttons are turned disabled (add no webcam found)
            if (listOfWebcams.isEmpty()) {
                disableButtons();
                webcamGUI.getBoxOfDevices().addItem("No webcam found");
            } else {
                for (Object listOfWebcam : listOfWebcams) {
                    webcamGUI.getBoxOfDevices().addItem((String) listOfWebcam);
                }
                enableButtons();
            }
        }

    }
}
