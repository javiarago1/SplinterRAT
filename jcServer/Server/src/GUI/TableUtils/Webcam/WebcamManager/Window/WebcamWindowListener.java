package GUI.TableUtils.Webcam.WebcamManager.Window;

import GUI.TableUtils.Webcam.WebcamManager.WebcamGUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*
 * Class for window closing actions:
 * - When is necessary to stop recording,streaming or save.
 */

public class WebcamWindowListener extends WindowAdapter {
    private final WebcamGUI webcamGUI;

    public WebcamWindowListener(WebcamGUI webcamGUI) {
        this.webcamGUI = webcamGUI;
    }

    @Override
    public void windowClosing(WindowEvent we) {
        // Case where a recording could be saved
        if (webcamGUI.getSaveRecordButton().isEnabled()) {
            webcamGUI.getStartButton().doClick();
            webcamGUI.getWebcamDialog().dispose();
            //webcamGUI.getStream().setWebcamDialogOpen(false);
        } else if (webcamGUI.getRecordButton().isSelected()) { // Case where is currently recording
            JOptionPane.showMessageDialog(
                    webcamGUI.getWebcamDialog(),
                    "You are currently recording, stop recording before exiting.",
                    "Stop recording",
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (webcamGUI.getStartButton().isSelected()) { // Case where is currently streaming
            webcamGUI.getStartButton().doClick();
            webcamGUI.getWebcamDialog().dispose();
            //webcamGUI.getStream().setWebcamDialogOpen(false);
        } else {  // Normal behaviour
            webcamGUI.getWebcamDialog().dispose();
            //webcamGUI.getStream().setWebcamDialogOpen(false);

        }

    }
}
