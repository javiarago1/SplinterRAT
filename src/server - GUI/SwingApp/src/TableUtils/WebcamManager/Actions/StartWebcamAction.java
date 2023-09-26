package TableUtils.WebcamManager.Actions;

import TableUtils.WebcamManager.Events.StartWebcamEvent;
import TableUtils.WebcamManager.Events.SaveRecordEvent;
import TableUtils.WebcamManager.Events.StopWebcamEvent;
import TableUtils.WebcamManager.WebcamGUI;
import Utilities.AbstractActionGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

/*
 * Class for start or stop webcam actions
 */
public class StartWebcamAction extends AbstractActionGUI<WebcamGUI> {

    public StartWebcamAction(WebcamGUI guiManager) {
        super(guiManager);
    }


    // Method for showing dialog to save or not save the currents recordings
    private void requestSave() {
        if (JOptionPane.showConfirmDialog(null,
                "You have pending recordings to save. Do you want to save the recordings?",
                "Warning: pending recordings to save",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            getGUIManager().getClient().getExecutor().submit(new SaveRecordEvent(getGUIManager()));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Recompiling information for StartWebcam class (Information.Information of configuration for client side)
        getGUIManager().getWebcamLabel().setText("");
        getGUIManager().setSelectedDevice(String.valueOf(getGUIManager().getBoxOfDevices().getSelectedItem()));
        getGUIManager().setFragmented(getGUIManager().getVideoFragmentedCheckBox().isSelected());

        AbstractButton abstractButton = (AbstractButton) e.getSource();
        boolean selected = abstractButton.getModel().isSelected();
        // enabling/disabling buttons
        if (selected) {
            getGUIManager().getBoxOfDevices().setEnabled(false);
            getGUIManager().getRecordButton().setEnabled(true);
            getGUIManager().getSnapshotButton().setEnabled(true);
            getGUIManager().getRecordingMenu().setEnabled(false);
            getGUIManager().getStartButton().setText("Stop webcam");
            // Sending the information and starting the webcam
            getGUIManager().getClient().getExecutor().submit(new StartWebcamEvent(getGUIManager()));
        } else {
            // Check if there are recordings to be saved
            if (getGUIManager().getSaveRecordButton().isEnabled()) requestSave();
            getGUIManager().getBoxOfDevices().setEnabled(true);
            getGUIManager().getRecordButton().setEnabled(false);
            getGUIManager().getSnapshotButton().setEnabled(false);
            getGUIManager().getRecordingMenu().setEnabled(true);
            getGUIManager().getSaveRecordButton().setEnabled(false);
            getGUIManager().getStartButton().setText("Start webcam");
            getGUIManager().getClient().getExecutor().submit(new StopWebcamEvent(getGUIManager()));
        }

    }
}
