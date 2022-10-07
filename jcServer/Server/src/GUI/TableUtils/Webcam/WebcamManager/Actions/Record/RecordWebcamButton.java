package GUI.TableUtils.Webcam.WebcamManager.Actions.Record;

import GUI.TableUtils.Webcam.WebcamManager.Actions.WebcamActions;
import GUI.TableUtils.Webcam.WebcamManager.WebcamGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

/*
 * Class for record or stop recording button actions
 */

public class RecordWebcamButton extends WebcamActions {


    public RecordWebcamButton(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractButton abstractButton = (AbstractButton) e.getSource();
        boolean selected = abstractButton.getModel().isSelected();
        if (selected) {
            getWebcamGUI().setStateStartRecordButton(true);
            getWebcamGUI().getSaveRecordButton().setEnabled(false);
            getWebcamGUI().getStartButton().setEnabled(false);
            getWebcamGUI().getRecordButton().setText("Stop recording");
        } else {
            getWebcamGUI().setStateStartRecordButton(false);
            getWebcamGUI().setStateStopRecordButton(true);
            getWebcamGUI().getSaveRecordButton().setEnabled(true);
            getWebcamGUI().getStartButton().setEnabled(true);
            getWebcamGUI().getRecordButton().setText("Record");
        }
    }
}
