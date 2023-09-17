package GUI.TableUtils.Webcam.WebcamManager.Actions;

import GUI.TableUtils.Webcam.WebcamManager.Events.StopWebcamEvent;
import GUI.TableUtils.Webcam.WebcamManager.Events.StartWebcamEvent;
import GUI.TableUtils.Webcam.WebcamManager.WebcamGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

/*
 * Class for start or stop webcam actions
 */
public class StartWebcamButton extends WebcamActions {


    public StartWebcamButton(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }


    // Method for showing dialog to save or not save the currents recordings
    private void requestSave() {
        if (JOptionPane.showConfirmDialog(null,
                "You have pending recordings to save. Do you want to save the recordings?",
                "Warning: pending recordings to save",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            getWebcamGUI().setSaveAndStop(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Recompiling information for StartWebcam class (Information of configuration for client side)
        getWebcamGUI().getWebcamLabel().setText("");
        getWebcamGUI().setSelectedDevice(String.valueOf(getWebcamGUI().getBoxOfDevices().getSelectedItem()));
        getWebcamGUI().setFragmented(getWebcamGUI().getVideoFragmentedCheckBox().isSelected());

        AbstractButton abstractButton = (AbstractButton) e.getSource();
        boolean selected = abstractButton.getModel().isSelected();
        // enabling/disabling buttons
        if (selected) {
            getWebcamGUI().setStateStreamingButton(true);
            getWebcamGUI().getBoxOfDevices().setEnabled(false);
            getWebcamGUI().getRecordButton().setEnabled(true);
            getWebcamGUI().getSnapshotButton().setEnabled(true);
            getWebcamGUI().getRecordingMenu().setEnabled(false);
            getWebcamGUI().getStartButton().setText("Stop webcam");
            // Sending the information and starting the webcam
            getWebcamGUI().getClient().getExecutor().submit(new StartWebcamEvent(getWebcamGUI()));
        } else {
            // Check if there are recordings to be saved
            if (getWebcamGUI().getSaveRecordButton().isEnabled()) requestSave();
            getWebcamGUI().setStateStreamingButton(false);
            getWebcamGUI().getBoxOfDevices().setEnabled(true);
            getWebcamGUI().getRecordButton().setEnabled(false);
            getWebcamGUI().getSnapshotButton().setEnabled(false);
            getWebcamGUI().getRecordingMenu().setEnabled(true);
            getWebcamGUI().getSaveRecordButton().setEnabled(false);
            getWebcamGUI().getStartButton().setText("Start webcam");
            getWebcamGUI().getClient().getExecutor().submit(new StopWebcamEvent(getWebcamGUI()));
        }

    }
}
