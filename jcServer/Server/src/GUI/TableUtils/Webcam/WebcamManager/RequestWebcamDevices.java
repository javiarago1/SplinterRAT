package GUI.TableUtils.Webcam.WebcamManager;

import Information.Action;

import javax.swing.*;
import java.util.List;


/*
 * Requesting webcam devices through sockets and getting
 * a list containing them.
 */
public class RequestWebcamDevices extends SwingWorker<Void, Void> {
    private final WebcamGUI webcamGUI;

    public RequestWebcamDevices(WebcamGUI webcamGUI) {
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
        listOfWebcams = (List<?>) webcamGUI.getStream().sendAndReadJSON(Action.REQUEST_WEBCAM);
        return null;
    }

    @Override
    protected void done() {
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
